package firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Robot;

public class Drive {
    private final Robot robot;
    private double lastFl, lastFr, lastBl, lastBr;

    public Drive(Robot robot) {
        this.robot = robot;
    }

    public void drive(double strafe, double forward, double turn, boolean robotCentric, double scale) {
        double heading = robotCentric ? 0.0 : robot.getHeadingRadians();
        double cos = Math.cos(-heading);
        double sin = Math.sin(-heading);
        double fieldStrafe = strafe * cos - forward * sin;
        double fieldForward = strafe * sin + forward * cos;

        fieldStrafe = applyDeadband(fieldStrafe, Config.DRIVE_DEADBAND);
        fieldForward = applyDeadband(fieldForward, Config.DRIVE_DEADBAND);
        turn = applyDeadband(turn, Config.DRIVE_DEADBAND);

        double denominator = Math.max(1.0, Math.abs(fieldForward) + Math.abs(fieldStrafe) + Math.abs(turn));
        double fl = (fieldForward + fieldStrafe + turn) / denominator;
        double fr = (fieldForward - fieldStrafe - turn) / denominator;
        double bl = (fieldForward - fieldStrafe + turn) / denominator;
        double br = (fieldForward + fieldStrafe - turn) / denominator;

        double max = Range.clip(scale, 0.0, 1.0) * Config.MAX_DRIVE_POWER;
        setRamped(fl * max, fr * max, bl * max, br * max);
    }

    public void setRaw(double fl, double fr, double bl, double br) {
        lastFl = Range.clip(fl, -1, 1);
        lastFr = Range.clip(fr, -1, 1);
        lastBl = Range.clip(bl, -1, 1);
        lastBr = Range.clip(br, -1, 1);
        setPowers(lastFl, lastFr, lastBl, lastBr);
    }

    public void stop() {
        setRaw(0, 0, 0, 0);
    }

    private void setRamped(double fl, double fr, double bl, double br) {
        lastFl = ramp(lastFl, fl);
        lastFr = ramp(lastFr, fr);
        lastBl = ramp(lastBl, bl);
        lastBr = ramp(lastBr, br);
        setPowers(lastFl, lastFr, lastBl, lastBr);
    }

    private void setPowers(double fl, double fr, double bl, double br) {
        robot.frontLeft.setPower(fl);
        robot.frontRight.setPower(fr);
        robot.backLeft.setPower(bl);
        robot.backRight.setPower(br);
    }

    private double ramp(double current, double target) {
        double delta = Range.clip(target - current, -Config.DRIVE_RAMP_PER_LOOP, Config.DRIVE_RAMP_PER_LOOP);
        return Range.clip(current + delta, -1, 1);
    }

    private double applyDeadband(double value, double deadband) {
        if (Math.abs(value) <= deadband) return 0.0;
        double sign = Math.signum(value);
        return sign * ((Math.abs(value) - deadband) / (1.0 - deadband));
    }
}
