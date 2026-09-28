package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Drive;
import org.firstinspires.ftc.teamcode.Intake;
import org.firstinspires.ftc.teamcode.Localizer;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.Shooter;
import org.firstinspires.ftc.teamcode.Transfer;

@Autonomous(name = "BIOBUZZ Auto", group = "Competition")
public class Auto extends LinearOpMode {
    private final Robot robot = new Robot();
    private Drive drive;
    private Intake intake;
    private Transfer transfer;
    private Shooter shooter;
    private Localizer localizer;
    private Limelight3A limelight;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        drive = new Drive(robot);
        intake = new Intake(robot);
        transfer = new Transfer(robot);
        shooter = new Shooter(robot);
        localizer = new Localizer(robot);

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        limelight.start();

        intake.setHomePosition();
        telemetry.addLine("AUTO READY");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;

        localizer.resetPose(0, 0, 0);
        runRoutine();

        limelight.stop();
        robot.stopAll();
    }

    private void runRoutine() {
        driveToPose(0, 24, 0, 4500);
        intake.setIntakePosition();
        holdMechanism(250);
        shooter.setPower(0);

        driveToPose(0, 36, 0, 2500);

        alignWithLimelight(0.0, 0.0, 2000);

        transfer.setTransferPosition();
        holdMechanism(250);
        transfer.setScorePosition();
        shooter.setPower(1.0);
        holdMechanism(650);
        shooter.setPower(0);
        holdMechanism(200);
        intake.setHomePosition();
        holdMechanism(200);
    }

    private boolean alignWithLimelight(double targetTx, double targetTy, long timeoutMs) {
        ElapsedTime timer = new ElapsedTime();
        timer.reset();

        double KpAim = 0.04;
        double KpDistance = 0.06;

        while (opModeIsActive() && timer.milliseconds() < timeoutMs) {
            robot.clearBulkCache();
            shooter.update();
            LLResult result = limelight.getLatestResult();

            if (result != null && result.isValid()) {
                double tx = result.getTx();
                double ty = result.getTy();

                double headingError = targetTx - tx;
                double distanceError = targetTy - ty;

                if (Math.abs(headingError) < 1.5 && Math.abs(distanceError) < 1.5) {
                    drive.stop();
                    return true;
                }

                double turn = Range.clip(headingError * KpAim, -Config.AUTO_MAX_TURN_POWER, Config.AUTO_MAX_TURN_POWER);
                double forward = Range.clip(distanceError * KpDistance, -Config.AUTO_MAX_TRANSLATION_POWER, Config.AUTO_MAX_TRANSLATION_POWER);

                drive.drive(0, forward, -turn, false, 1.0);

                telemetry.addData("Limelight", "Tracking target");
                telemetry.addData("tx / ty", "%.2f / %.2f", tx, ty);
            } else {
                drive.stop();
                telemetry.addData("Limelight", "Searching for target...");
            }
            telemetry.update();
        }

        drive.stop();
        return false;
    }

    private boolean driveToPose(double targetX, double targetY, double targetHeadingDeg, long timeoutMs) {
        ElapsedTime timer = new ElapsedTime();
        timer.reset();
        double targetHeading = Math.toRadians(targetHeadingDeg);

        while (opModeIsActive() && timer.milliseconds() < timeoutMs) {
            robot.clearBulkCache();
            localizer.update();
            Localizer.Pose pose = localizer.getPose();

            double errorX = targetX - pose.x;
            double errorY = targetY - pose.y;
            double distance = Math.hypot(errorX, errorY);
            double headingError = normalize(targetHeading - pose.heading);

            if (distance <= Config.AUTO_POSITION_TOLERANCE_IN && Math.abs(Math.toDegrees(headingError)) <= Config.AUTO_HEADING_TOLERANCE_DEG) {
                drive.stop();
                return true;
            }

            double fieldStrafe = Range.clip(errorX * Config.AUTO_X_KP, -Config.AUTO_MAX_TRANSLATION_POWER, Config.AUTO_MAX_TRANSLATION_POWER);
            double fieldForward = Range.clip(errorY * Config.AUTO_Y_KP, -Config.AUTO_MAX_TRANSLATION_POWER, Config.AUTO_MAX_TRANSLATION_POWER);
            double turn = Range.clip(Math.toDegrees(headingError) * Config.AUTO_HEADING_KP, -Config.AUTO_MAX_TURN_POWER, Config.AUTO_MAX_TURN_POWER);

            drive.drive(fieldStrafe, fieldForward, turn, false, 1.0);
            shooter.update();

            telemetry.addData("X / Y", "%.2f / %.2f", pose.x, pose.y);
            telemetry.addData("Heading", "%.1f", Math.toDegrees(pose.heading));
            telemetry.update();
        }

        drive.stop();
        return false;
    }

    private void holdMechanism(long milliseconds) {
        ElapsedTime timer = new ElapsedTime();
        timer.reset();
        while (opModeIsActive() && timer.milliseconds() < milliseconds) {
            robot.clearBulkCache();
            shooter.update();
            localizer.update();
            idle();
        }
    }

    private double normalize(double angle) {
        while (angle > Math.PI) angle -= 2 * Math.PI;
        while (angle < -Math.PI) angle += 2 * Math.PI;
        return angle;
    }
}
