package firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

public class Localizer {
    public static class Pose {
        public double x;
        public double y;
        public double heading;

        public Pose(double x, double y, double heading) {
            this.x = x;
            this.y = y;
            this.heading = heading;
        }
    }

    private final Robot robot;
    private int lastParallel;
    private int lastPerpendicular;
    private double lastHeading;
    private final Pose pose = new Pose(0, 0, 0);

    public Localizer(Robot robot) {
        this.robot = robot;
        resetPose(0, 0, 0);
    }

    public void resetPose(double x, double y, double heading) {
        robot.odoParallel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.odoParallel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.odoPerpendicular.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.odoPerpendicular.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.resetHeading();
        pose.x = x;
        pose.y = y;
        pose.heading = heading;
        lastParallel = robot.odoParallel.getCurrentPosition();
        lastPerpendicular = robot.odoPerpendicular.getCurrentPosition();
        lastHeading = heading;
    }

    public void update() {
        int parallel = robot.odoParallel.getCurrentPosition();
        int perpendicular = robot.odoPerpendicular.getCurrentPosition();
        double heading = robot.getHeadingRadians();

        double dParallel = ticksToInches(parallel - lastParallel) * Config.ODO_FORWARD_SIGN;
        double dPerpendicular = ticksToInches(perpendicular - lastPerpendicular) * Config.ODO_LATERAL_SIGN;
        double dHeading = normalize(heading - lastHeading);

        double forward = dParallel - Config.ODO_FORWARD_OFFSET_IN * dHeading;
        double strafe = dPerpendicular - Config.ODO_LATERAL_OFFSET_IN * dHeading;
        double midpoint = lastHeading + dHeading * 0.5;

        double dxField = strafe * Math.cos(midpoint) + forward * Math.sin(midpoint);
        double dyField = -strafe * Math.sin(midpoint) + forward * Math.cos(midpoint);

        pose.x += dxField;
        pose.y += dyField;
        pose.heading = heading;

        lastParallel = parallel;
        lastPerpendicular = perpendicular;
        lastHeading = heading;
    }

    public Pose getPose() {
        return new Pose(pose.x, pose.y, pose.heading);
    }

    private double ticksToInches(double ticks) {
        return ticks / Config.ODO_TICKS_PER_REV * Math.PI * Config.ODO_WHEEL_DIAMETER_IN;
    }

    private double normalize(double angle) {
        while (angle > Math.PI) angle -= 2.0 * Math.PI;
        while (angle < -Math.PI) angle += 2.0 * Math.PI;
        return Range.clip(angle, -Math.PI, Math.PI);
    }
}
