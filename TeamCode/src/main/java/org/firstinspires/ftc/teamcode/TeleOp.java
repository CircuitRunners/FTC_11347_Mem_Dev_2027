package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "BIOBUZZ TeleOp", group = "Competition")
public class TeleOp extends OpMode {
    public enum Preset { HOME, INTAKE, TRANSFER, SCORE }

    private final Robot robot = new Robot();
    private Drive drive;
    private Intake intake;
    private Transfer transfer;
    private Shooter shooter;

    private Preset currentPreset = Preset.HOME;
    private boolean lastStart, lastA, lastB, lastX, lastY, lastDpadUp, lastDpadDown;

    @Override
    public void init() {
        robot.init(hardwareMap);
        drive = new Drive(robot);
        intake = new Intake(robot);
        transfer = new Transfer(robot);
        shooter = new Shooter(robot);

        telemetry.addLine("BIOBUZZ READY");
        telemetry.update();
    }

    @Override
    public void start() {
        robot.resetHeading();
        intake.setHomePosition();
        shooter.setPower(0);
    }

    @Override
    public void loop() {
        robot.clearBulkCache();

        if (gamepad1.start && !lastStart) robot.resetHeading();
        lastStart = gamepad1.start;

        double strafe = -gamepad1.left_stick_x;
        double forward = -gamepad1.left_stick_y;
        double turn = -gamepad1.right_stick_x;
        boolean robotCentric = gamepad1.right_bumper;
        double scale = gamepad1.left_bumper ? Config.SLOW_DRIVE_POWER / Config.MAX_DRIVE_POWER : 1.0;
        drive.drive(strafe, forward, turn, robotCentric, scale);

        if (gamepad2.a && !lastA) {
            intake.setHomePosition();
            currentPreset = Preset.HOME;
        }
        if (gamepad2.b && !lastB) {
            intake.setIntakePosition();
            currentPreset = Preset.INTAKE;
        }
        if (gamepad2.x && !lastX) {
            transfer.setTransferPosition();
            currentPreset = Preset.TRANSFER;
        }
        if (gamepad2.y && !lastY) {
            transfer.setScorePosition();
            currentPreset = Preset.SCORE;
        }
        lastA = gamepad2.a;
        lastB = gamepad2.b;
        lastX = gamepad2.x;
        lastY = gamepad2.y;

        if (gamepad2.dpad_up && !lastDpadUp) shooter.setPower(1.0);
        if (gamepad2.dpad_down && !lastDpadDown) shooter.setPower(0.0);
        lastDpadUp = gamepad2.dpad_up;
        lastDpadDown = gamepad2.dpad_down;

        double manualShooter = gamepad2.right_trigger - gamepad2.left_trigger;
        if (Math.abs(manualShooter) > 0.05) shooter.setPower(manualShooter);
        shooter.update();

        telemetry.addData("Heading Deg", "%.1f", Math.toDegrees(robot.getHeadingRadians()));
        telemetry.addData("Drive Mode", robotCentric ? "Robot" : "Field");
        telemetry.addData("Preset", currentPreset);
        telemetry.addData("Shooter Power", "%.2f / %.2f", shooter.getCommandedPower(), shooter.getTargetPower());
        telemetry.addData("Shooter Current", "%.2f A", shooter.getCurrentAmps());
        telemetry.update();
    }

    @Override
    public void stop() {
        robot.stopAll();
    }
}
