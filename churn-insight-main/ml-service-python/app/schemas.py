from enum import Enum
from pydantic import BaseModel, Field


class Gender(str, Enum):
    FEMALE = "FEMALE"
    MALE = "MALE"


class ContractType(str, Enum):
    MONTH_TO_MONTH = "MONTH_TO_MONTH"
    ONE_YEAR = "ONE_YEAR"
    TWO_YEAR = "TWO_YEAR"


class InternetService(str, Enum):
    DSL = "DSL"
    FIBER_OPTIC = "FIBER_OPTIC"
    NONE = "NONE"


class PaymentMethod(str, Enum):
    ELECTRONIC_CHECK = "ELECTRONIC_CHECK"
    MAILED_CHECK = "MAILED_CHECK"
    BANK_TRANSFER_AUTOMATIC = "BANK_TRANSFER_AUTOMATIC"
    CREDIT_CARD_AUTOMATIC = "CREDIT_CARD_AUTOMATIC"


class MultipleLines(str, Enum):
    NO = "NO"
    YES = "YES"
    NO_PHONE_SERVICE = "NO_PHONE_SERVICE"


class YesNoInternet(str, Enum):
    NO = "NO"
    YES = "YES"
    NO_INTERNET_SERVICE = "NO_INTERNET_SERVICE"


class CustomerInput(BaseModel):
    gender: Gender

    seniorCitizen: bool
    partner: bool
    dependents: bool

    contractMonths: int = Field(ge=0, le=72)

    phoneService: bool
    multipleLines: MultipleLines

    internetService: InternetService

    onlineSecurity: YesNoInternet
    onlineBackup: YesNoInternet
    deviceProtection: YesNoInternet
    techSupport: YesNoInternet
    streamingTV: YesNoInternet
    streamingMovies: YesNoInternet

    contractType: ContractType
    paperlessBilling: bool
    paymentMethod: PaymentMethod

    monthlyCharges: float = Field(ge=0)
    totalCharges: float = Field(ge=0)


class PredictionResponse(BaseModel):
    prediction: str
    probability: float
