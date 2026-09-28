package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.Range;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "BIOBUZZ Hardware Diagnostics", group = "Diagnostics")
public class Diagnostics extends LinearOpMode {
    private final Robot robot = new Robot();

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        int servoIndex = 0;
        double[] positions = Config.HOME.clone();
        boolean lastLeft = false, lastRight = false, lastUp = false, lastDown = false;
        boolean lastA = false, lastB = false, lastX = false, lastY = false;

        telemetry.addLine("DIAGNOSTICS READY");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            robot.clearBulkCache();

            if (gamepad1.dpad_left && !lastLeft) servoIndex = Math.max(0, servoIndex - 1);
            if (gamepad1.dpad_right && !lastRight) servoIndex = Math.min(6, servoIndex + 1);
            if (gamepad1.dpad_up && !lastUp) positions[servoIndex] = Range.clip(positions[servoIndex] + 0.02, Config.SERVO_MIN, Config.SERVO_MAX);
            if (gamepad1.dpad_down && !lastDown) positions[servoIndex] = Range.clip(positions[servoIndex] - 0.02, Config.SERVO_MIN, Config.SERVO_MAX);
            if (gamepad1.a && !lastA) robot.setServoPositions(Config.HOME);
            if (gamepad1.b && !lastB) robot.setServoPositions(Config.INTAKE);
            if (gamepad1.x && !lastX) robot.setServoPositions(Config.TRANSFER);
            if (gamepad1.y && !lastY) robot.setServoPositions(Config.SCORE);

            lastLeft = gamepad1.dpad_left;
            lastRight = gamepad1.dpad_right;
            lastUp = gamepad1.dpad_up;
            lastDown = gamepad1.dpad_down;
            lastA = gamepad1.a;
            lastB = gamepad1.b;
            lastX = gamepad1.x;
            lastY = gamepad2.y;

            robot.servos[servoIndex].setPosition(positions[servoIndex]);

            double drivePower = -gamepad2.left_stick_y * 0.20;
            if (Math.abs(drivePower) < 0.04) drivePower = 0;
            robot.frontLeft.setPower(drivePower);
            robot.frontRight.setPower(drivePower);
            robot.backLeft.setPower(drivePower);
            robot.backRight.setPower(drivePower);

            double shooterPower = gamepad2.right_trigger - gamepad2.left_trigger;
            robot.outtakeLeft.setPower(shooterPower);
            robot.outtakeRight.setPower(shooterPower);

            telemetry.addData("Servo Index", servoIndex + 1);
            telemetry.addData("Position", "%.3f", positions[servoIndex]);
            telemetry.addData("Heading Deg", "%.1f", Math.toDegrees(robot.getHeadingRadians()));
            telemetry.update();
        }

        robot.stopAll();
    }
}
