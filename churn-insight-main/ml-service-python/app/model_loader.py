import pickle
from app.settings import settings

_model = None

def get_model():
    global _model
    if _model is None:
        with open(settings.model_path, "rb") as f:
            _model = pickle.load(f)
    return _model
