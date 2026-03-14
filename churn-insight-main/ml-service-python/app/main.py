from fastapi import FastAPI
from pathlib import Path
import pandas as pd
import pickle

from app.schemas import CustomerInput, PredictionResponse
from app.settings import settings

app = FastAPI(title="Churn ML Service", version="0.1.0")

# === Carrega artefato  ===
BASE_DIR = Path(__file__).resolve().parent.parent  # .../ml-service-python
ARTIFACT_PATH = BASE_DIR / "notebooks" / "rf_artifact.pkl"

model = None
scaler = None
FEATURE_NAMES = []

try:
    with open(ARTIFACT_PATH, "rb") as f:
        artifact = pickle.load(f)
    model = artifact.get("model")
    scaler = artifact.get("scaler")
    FEATURE_NAMES = artifact.get("feature_names", [])
except Exception:
    # If artifact is missing or invalid, keep model None and allow service to start.
    artifact = None

NUMERIC_COLS = ["contract_months", "monthly_charges", "total_charges"]


@app.get("/health")
def health():
    status = "ok" if model is not None else "degraded"
    return {"status": status}


def _set_one_hot(row: dict, prefix: str, value: str):
    col = f"{prefix}_{value}"
    if col in row:
        row[col] = 1


def norm(v) -> str:
    return v.value.lower() if hasattr(v, "value") else str(v).lower()


def to_features(dto: CustomerInput) -> dict:
    row = {name: 0 for name in FEATURE_NAMES}

    # diretas
    row["gender"] = 1 if dto.gender.value == "MALE" else 0
    row["senior_citizen"] = 1 if dto.seniorCitizen else 0
    row["partner"] = 1 if dto.partner else 0
    row["dependents"] = 1 if dto.dependents else 0

    row["contract_months"] = dto.contractMonths
    row["phone_service"] = 1 if dto.phoneService else 0
    row["paperless_billing"] = 1 if dto.paperlessBilling else 0

    row["monthly_charges"] = dto.monthlyCharges
    row["total_charges"] = dto.totalCharges

    # multiple_lines
    _set_one_hot(row, "multiple_lines", norm(dto.multipleLines))

    # internet_service (NONE vira "_no")
    internet = norm(dto.internetService)  # dsl | fiber_optic | none
    _set_one_hot(row, "internet_service", "no" if internet == "none" else internet)

    # tri-estado
    _set_one_hot(row, "online_security", norm(dto.onlineSecurity))
    _set_one_hot(row, "online_backup", norm(dto.onlineBackup))
    _set_one_hot(row, "device_protection", norm(dto.deviceProtection))
    _set_one_hot(row, "tech_support", norm(dto.techSupport))
    _set_one_hot(row, "streaming_tv", norm(dto.streamingTV))
    _set_one_hot(row, "streaming_movies", norm(dto.streamingMovies))

    # contract
    _set_one_hot(row, "contract", norm(dto.contractType))

    # payment_method
    _set_one_hot(row, "payment_method", norm(dto.paymentMethod))

    return row


@app.post("/predict", response_model=PredictionResponse)
def predict(input: CustomerInput):
    if model is None or scaler is None or not FEATURE_NAMES:
        # Service not ready to predict
        from fastapi import HTTPException
        raise HTTPException(status_code=503, detail="Model artifact not available")

    row = to_features(input)

    # DataFrame na ordem esperada
    X = pd.DataFrame([[row[c] for c in FEATURE_NAMES]], columns=FEATURE_NAMES)

    # scaler nas numéricas
    X[NUMERIC_COLS] = scaler.transform(X[NUMERIC_COLS])

    proba = float(model.predict_proba(X)[0][1])
    prediction = "Churn" if proba >= settings.threshold else "No Churn"

    return PredictionResponse(prediction=prediction, probability=round(proba, 4))
