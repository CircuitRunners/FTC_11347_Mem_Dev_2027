package competition;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Config.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Config.MecanumDrivebase;

@TeleOp(name = "Mecanum & Intake TeleOp", group = "TeleOp")
public class MecanumTeleOp extends OpMode {

    private MecanumDrivebase drivebase;
    private IntakeSubsystem intake;

    @Override
    public void init() {
        drivebase = new MecanumDrivebase(hardwareMap);
        intake = new IntakeSubsystem(hardwareMap, telemetry);

        telemetry.addData("Status", "Initialized!");
        telemetry.update();
    }

    @Override
    public void loop() {
        double forward = -gamepad1.left_stick_y;
        double right   = gamepad1.left_stick_x;
        double rotate  = gamepad1.right_stick_x;

        drivebase.drive(forward, right, rotate);

        double intakePower = gamepad1.right_trigger - gamepad1.left_trigger;

        if (Math.abs(intakePower) > 0.05) {
            intake.intake(intakePower);
        } else {
            intake.stop();
        }

        telemetry.addData("Drive Forward", forward);
        telemetry.addData("Drive Right", right);
        telemetry.addData("Drive Rotate", rotate);
        telemetry.addData("Intake Power", intakePower);
        telemetry.update();
    }
}
