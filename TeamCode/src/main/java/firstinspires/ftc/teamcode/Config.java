package firstinspires.ftc.teamcode;

public final class Config {
    private Config() {}

    public static final String FL = "frontLeft";
    public static final String FR = "frontRight";
    public static final String BL = "backLeft";
    public static final String BR = "backRight";

    public static final String OUTTAKE_LEFT = "outtakeLeft";
    public static final String OUTTAKE_RIGHT = "outtakeRight";

    public static final String AXON_1 = "axon1";
    public static final String AXON_2 = "axon2";
    public static final String AXON_3 = "axon3";
    public static final String AXON_4 = "axon4";
    public static final String SERVO_25_1 = "servo25_1";
    public static final String SERVO_25_2 = "servo25_2";
    public static final String SERVO_25_3 = "servo25_3";

    public static final String IMU = "imu";
    public static final String COLOR = "color";
    public static final String ODO_PARALLEL = "odoParallel";
    public static final String ODO_PERPENDICULAR = "odoPerpendicular";

    public static final boolean REVERSE_FL = true;
    public static final boolean REVERSE_FR = false;
    public static final boolean REVERSE_BL = true;
    public static final boolean REVERSE_BR = false;
    public static final boolean REVERSE_OUTTAKE_LEFT = true;
    public static final boolean REVERSE_OUTTAKE_RIGHT = false;

    public static final boolean REVERSE_AXON_1 = false;
    public static final boolean REVERSE_AXON_2 = false;
    public static final boolean REVERSE_AXON_3 = false;
    public static final boolean REVERSE_AXON_4 = false;
    public static final boolean REVERSE_SERVO_25_1 = false;
    public static final boolean REVERSE_SERVO_25_2 = false;
    public static final boolean REVERSE_SERVO_25_3 = false;

    public static final double MAX_DRIVE_POWER = 0.92;
    public static final double SLOW_DRIVE_POWER = 0.35;
    public static final double DRIVE_DEADBAND = 0.04;
    public static final double DRIVE_RAMP_PER_LOOP = 0.08;

    public static final double ODO_TICKS_PER_REV = 2000.0;
    public static final double ODO_WHEEL_DIAMETER_IN = 32.0 / 25.4;
    public static final double ODO_FORWARD_OFFSET_IN = 0.0;
    public static final double ODO_LATERAL_OFFSET_IN = 0.0;
    public static final double ODO_FORWARD_SIGN = 1.0;
    public static final double ODO_LATERAL_SIGN = 1.0;

    public static final double OUTTAKE_MAX_POWER = 1.0;
    public static final double OUTTAKE_SLEW_PER_LOOP = 0.15;
    public static final double OUTTAKE_CURRENT_LIMIT_AMPS = 8.0;
    public static final long OUTTAKE_STALL_TIME_MS = 350;

    public static final double SERVO_MIN = 0.02;
    public static final double SERVO_MAX = 0.98;

    public static final double[] HOME = {0.50, 0.50, 0.50, 0.50, 0.50, 0.50, 0.50};
    public static final double[] INTAKE = {0.20, 0.80, 0.20, 0.80, 0.20, 0.80, 0.50};
    public static final double[] TRANSFER = {0.65, 0.35, 0.65, 0.35, 0.50, 0.50, 0.50};
    public static final double[] SCORE = {0.80, 0.20, 0.80, 0.20, 0.80, 0.20, 0.50};

    public static final double AUTO_MAX_TRANSLATION_POWER = 0.70;
    public static final double AUTO_MAX_TURN_POWER = 0.55;
    public static final double AUTO_X_KP = 0.07;
    public static final double AUTO_Y_KP = 0.07;
    public static final double AUTO_HEADING_KP = 0.018;
    public static final double AUTO_POSITION_TOLERANCE_IN = 1.0;
    public static final double AUTO_HEADING_TOLERANCE_DEG = 2.0;
}
