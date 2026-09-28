package firstinspires.ftc.teamcode;

public class Transfer {
    private final Robot robot;

    public Transfer(Robot robot) {
        this.robot = robot;
    }

    public void setTransferPosition() {
        robot.setServoPositions(Config.TRANSFER);
    }

    public void setScorePosition() {
        robot.setServoPositions(Config.SCORE);
    }
}
