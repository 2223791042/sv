package guet.sgc.sv.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Vacate {
    private Long leaveId;

    private String studentId;

    private String studentName;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date leaveTime;

    private String leaveReason;

    private String leaveFile;

    private Date submitTime;

    private Integer leaveStatus;

    public Long getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(Long leaveId) {
        this.leaveId = leaveId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId == null ? null : studentId.trim();
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName == null ? null : studentName.trim();
    }

    public Date getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(Date leaveTime) {
        this.leaveTime = leaveTime;
    }

    public String getLeaveReason() {
        return leaveReason;
    }

    public void setLeaveReason(String leaveReason) {
        this.leaveReason = leaveReason == null ? null : leaveReason.trim();
    }

    public String getLeaveFile() {
        return leaveFile;
    }

    public void setLeaveFile(String leaveFile) {
        this.leaveFile = leaveFile == null ? null : leaveFile.trim();
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public Integer getLeaveStatus() {
        return leaveStatus;
    }

    public void setLeaveStatus(Integer leaveStatus) {
        this.leaveStatus = leaveStatus;
    }
}