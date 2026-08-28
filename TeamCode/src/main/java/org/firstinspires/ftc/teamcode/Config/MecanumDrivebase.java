package org.firstinspires.ftc.teamcode.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;


public class MecanumDrivebase  {
    public DcMotorEx frontLeftMotor;
    public DcMotorEx frontRightMotor;
    public DcMotorEx backLeftMotor;
    public DcMotorEx backRightMotor;


    public MecanumDrivebase(HardwareMap hardwareMap) {
        frontLeftMotor = hardwareMap.get(DcMotorEx.class, "fl");
        frontRightMotor = hardwareMap.get(DcMotorEx.class, "fr");
        backLeftMotor = hardwareMap.get(DcMotorEx.class, "bl");
        backRightMotor = hardwareMap.get(DcMotorEx.class, "br");

        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        DcMotorEx[] motors = new DcMotorEx[]{
                frontLeftMotor,frontRightMotor, backLeftMotor, backRightMotor
        };


        for (DcMotorEx motor : motors) {
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
    }

    private void setPowers(
            double frontLeftPower,
            double frontRightPower,
            double backLeftPower,
            double backRightPower
    ) {
        double maxPower = Math.max(
                1.0,
                Math.max(
                        Math.abs(frontLeftPower),
                        Math.max(
                                Math.abs(frontRightPower),
                                Math.max(
                                        Math.abs(backLeftPower),
                                        Math.abs(backRightPower)
                                )
                        )
                )
        );

        frontLeftMotor.setPower(frontLeftPower / maxPower);
        frontRightMotor.setPower(frontRightPower / maxPower);
        backLeftMotor.setPower(backLeftPower / maxPower);
        backRightMotor.setPower(backRightPower / maxPower);
    }
    public void drive(double forward, double right, double rotate) {
        double frontLeftPower = forward + right + rotate;
        double frontRightPower = forward - right - rotate;
        double backLeftPower = forward - right + rotate;
        double backRightPower = forward + right - rotate;

        setPowers(frontLeftPower, frontRightPower, backLeftPower, backRightPower);
    }

}

