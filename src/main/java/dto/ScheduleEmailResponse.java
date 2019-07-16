package dto;

public class ScheduleEmailResponse {
    private boolean success;
    private String message;
    private String jobId;
    private String jobGroup;

    public ScheduleEmailResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ScheduleEmailResponse(boolean success, String message, String jobId, String jobGroup) {
        this.success = success;
        this.message = message;
        this.jobId = jobId;
        this.jobGroup = jobGroup;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }
}
