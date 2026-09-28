package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Config;

import java.util.List;

public class Robot {
    public DcMotorEx frontLeft, frontRight, backLeft, backRight;
    public DcMotorEx outtakeLeft, outtakeRight;
    public DcMotorEx odoParallel, odoPerpendicular;

    public Servo axon1, axon2, axon3, axon4;
    public Servo servo25_1, servo25_2, servo25_3;

    public IMU imu;
    public ColorSensor color;
    public List<LynxModule> hubs;

    public final Servo[] servos = new Servo[7];

    public void init(HardwareMap hardwareMap) {
        hubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : hubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        frontLeft = required(hardwareMap, DcMotorEx.class, Config.FL);
        frontRight = required(hardwareMap, DcMotorEx.class, Config.FR);
        backLeft = required(hardwareMap, DcMotorEx.class, Config.BL);
        backRight = required(hardwareMap, DcMotorEx.class, Config.BR);

        outtakeLeft = required(hardwareMap, DcMotorEx.class, Config.OUTTAKE_LEFT);
        outtakeRight = required(hardwareMap, DcMotorEx.class, Config.OUTTAKE_RIGHT);

        odoParallel = required(hardwareMap, DcMotorEx.class, Config.ODO_PARALLEL);
        odoPerpendicular = required(hardwareMap, DcMotorEx.class, Config.ODO_PERPENDICULAR);

        axon1 = required(hardwareMap, Servo.class, Config.AXON_1);
        axon2 = required(hardwareMap, Servo.class, Config.AXON_2);
        axon3 = required(hardwareMap, Servo.class, Config.AXON_3);
        axon4 = required(hardwareMap, Servo.class, Config.AXON_4);
        servo25_1 = required(hardwareMap, Servo.class, Config.SERVO_25_1);
        servo25_2 = required(hardwareMap, Servo.class, Config.SERVO_25_2);
        servo25_3 = required(hardwareMap, Servo.class, Config.SERVO_25_3);

        imu = required(hardwareMap, IMU.class, Config.IMU);
        color = required(hardwareMap, ColorSensor.class, Config.COLOR);

        frontLeft.setDirection(Config.REVERSE_FL ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
        frontRight.setDirection(Config.REVERSE_FR ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
        backLeft.setDirection(Config.REVERSE_BL ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
        backRight.setDirection(Config.REVERSE_BR ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);

        outtakeLeft.setDirection(Config.REVERSE_OUTTAKE_LEFT ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
        outtakeRight.setDirection(Config.REVERSE_OUTTAKE_RIGHT ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);

        configureMotor(frontLeft, DcMotor.RunMode.RUN_USING_ENCODER);
        configureMotor(frontRight, DcMotor.RunMode.RUN_USING_ENCODER);
        configureMotor(backLeft, DcMotor.RunMode.RUN_USING_ENCODER);
        configureMotor(backRight, DcMotor.RunMode.RUN_USING_ENCODER);

        configureMotor(outtakeLeft, DcMotor.RunMode.RUN_USING_ENCODER);
        configureMotor(outtakeRight, DcMotor.RunMode.RUN_USING_ENCODER);

        configureMotor(odoParallel, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        configureMotor(odoPerpendicular, DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        axon1.setDirection(Config.REVERSE_AXON_1 ? Servo.Direction.REVERSE : Servo.Direction.FORWARD);
        axon2.setDirection(Config.REVERSE_AXON_2 ? Servo.Direction.REVERSE : Servo.Direction.FORWARD);
        axon3.setDirection(Config.REVERSE_AXON_3 ? Servo.Direction.REVERSE : Servo.Direction.FORWARD);
        axon4.setDirection(Config.REVERSE_AXON_4 ? Servo.Direction.REVERSE : Servo.Direction.FORWARD);
        servo25_1.setDirection(Config.REVERSE_SERVO_25_1 ? Servo.Direction.REVERSE : Servo.Direction.FORWARD);
        servo25_2.setDirection(Config.REVERSE_SERVO_25_2 ? Servo.Direction.REVERSE : Servo.Direction.FORWARD);
        servo25_3.setDirection(Config.REVERSE_SERVO_25_3 ? Servo.Direction.REVERSE : Servo.Direction.FORWARD);

        servos[0] = axon1;
        servos[1] = axon2;
        servos[2] = axon3;
        servos[3] = axon4;
        servos[4] = servo25_1;
        servos[5] = servo25_2;
        servos[6] = servo25_3;

        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
        )));
        imu.resetYaw();
        setServoPositions(Config.HOME);
        clearBulkCache();
    }

    public void clearBulkCache() {
        if (hubs != null) {
            for (LynxModule hub : hubs) {
                hub.clearBulkCache();
            }
        }
    }

    private void configureMotor(DcMotorEx motor, DcMotor.RunMode mode) {
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(mode);
        motor.setPower(0);
    }

    public double getHeadingRadians() {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
    }

    public void resetHeading() {
        imu.resetYaw();
    }

    public void setServoPositions(double[] positions) {
        if (positions.length != servos.length) {
            throw new IllegalArgumentException("Expected 7 servo positions");
        }
        for (int i = 0; i < servos.length; i++) {
            servos[i].setPosition(Range.clip(positions[i], Config.SERVO_MIN, Config.SERVO_MAX));
        }
    }

    public void stopAll() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
        outtakeLeft.setPower(0);
        outtakeRight.setPower(0);
    }

    private <T> T required(HardwareMap hardwareMap, Class<? extends T> type, String name) {
        try {
            return hardwareMap.get(type, name);
        } catch (Exception e) {
            throw new IllegalStateException("Missing hardware: " + name, e);
        }
    }
}
