from pydantic import BaseModel

class Settings(BaseModel):
    model_path: str = "rf_artifact.pkl"
    threshold: float = 0.5

settings = Settings()
