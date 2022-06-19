package utils.others;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public class WorkHour {
    @JsonFormat(pattern="HH:mm:ss")
    LocalTime start;
    @JsonFormat(pattern="HH:mm:ss")
    LocalTime end;

    public WorkHour(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }
}
