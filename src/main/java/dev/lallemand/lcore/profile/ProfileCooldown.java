package dev.lallemand.lcore.profile;

import lombok.Data;
import dev.lallemand.lcore.util.TimeUtil;

@Data
public class ProfileCooldown {

    private long start = System.currentTimeMillis();
    private long expire;
    private boolean notified;

    public ProfileCooldown(long duration) {
        this.expire = this.start + duration;

        if (duration == 0) {
            this.notified = true;
        }
    }

    public long getPassed() {
        return System.currentTimeMillis() - this.start;
    }

    public long getRemaining() {
        return this.expire - System.currentTimeMillis();
    }

    public boolean hasExpired() {
        return System.currentTimeMillis() - this.expire >= 0;
    }

    public String getTimeLeft() {
        if (this.getRemaining() >= 60_000) {
            return TimeUtil.millisToRoundedTime(this.getRemaining());
        } else {
            return TimeUtil.millisToSeconds(this.getRemaining());
        }
    }

}
