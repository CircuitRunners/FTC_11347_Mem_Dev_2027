package org.firstinspires.ftc.teamcode.Config;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class IntakeSubsystem extends SubsystemBase {
    private final DcMotorEx intakeChubM, intakeEhubM;
    private final  CRServo intakeChubS, intakeEhubS;
    public final static boolean reverseChubServoDirection = false; // true
    public final static boolean reverseEhubServoDirection = false;

    public IntakeSubsystem(HardwareMap hardwareMap, Telemetry telemetry) {
        intakeChubM = hardwareMap.get(DcMotorEx.class, "intakeChub");
        intakeEhubM = hardwareMap.get(DcMotorEx.class, "intakeEhub");

        intakeChubS = new CRServo(hardwareMap, "intakeRed");
        intakeEhubS = new CRServo(hardwareMap, "intakeBlue");

        intakeChubM.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        intakeEhubM.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addLine("Intake Init Done");
    }

    public void intakeChub(double power) {
        intakeChubM.setPower(Range.clip(power, -0.9, 0.9));

        double sPower = reverseChubServoDirection ? -power : power;
        intakeEhubS.set(sPower);
    }

    public void intakeEhub(double power) {
        intakeEhubM.setPower(Range.clip(power, -0.9, 0.9));

        double sPower = reverseEhubServoDirection ? -power : power;
        intakeChubS.set(sPower);
    }

    public void intake(double power) {
        intakeChub(power);
        intakeEhub(power);
    }

    public void stop() {
        intake(0);
    }
}
