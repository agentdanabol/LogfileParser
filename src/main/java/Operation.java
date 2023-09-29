public class Operation {
    private int id;
    private String thread;
    private boolean status;
    private long operationTime;
    private long authenticationTime;
    private long authorizationTime;
    private long balanceModificationTime;

    public Operation() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public long getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(long operationTime) {
        this.operationTime = operationTime;
    }

    public long getAuthenticationTime() {
        return authenticationTime;
    }

    public void setAuthenticationTime(long authenticationTime) {
        this.authenticationTime = authenticationTime;
    }

    public long getAuthorizationTime() {
        return authorizationTime;
    }

    public void setAuthorizationTime(long authorizationTime) {
        this.authorizationTime = authorizationTime;
    }

    public long getBalanceModificationTime() {
        return balanceModificationTime;
    }

    public void setBalanceModificationTime(long balanceModificationTime) {
        this.balanceModificationTime = balanceModificationTime;
    }
}
