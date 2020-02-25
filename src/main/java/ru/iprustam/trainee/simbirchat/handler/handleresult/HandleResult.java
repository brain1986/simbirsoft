package ru.iprustam.trainee.simbirchat.handler.handleresult;

public class HandleResult {
    private HandleErrorType errorType;
    private HandleDetails handleDetails;

    {
        errorType = HandleErrorType.OK;
        handleDetails.setText("");
    }

    public HandleResult(){
    }

    public HandleResult(HandleErrorType errorType) {
        this.errorType = errorType;
    }

    public HandleResult(HandleErrorType errorType, HandleDetails handleDetails) {
        this.errorType = errorType;
        this.handleDetails = handleDetails;
    }

    public HandleErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(HandleErrorType errorType) {
        this.errorType = errorType;
    }

    public HandleDetails getHandleDetails() {
        return handleDetails;
    }

    public void setHandleDetails(HandleDetails handleDetails) {
        this.handleDetails = handleDetails;
    }
}
