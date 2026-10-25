package com.cricket.tournament.dto;

public class BatterFiguresResponse {


private Long batterId;
private String batterName;

private int runs;
private int ballsFaced;
private int fours;
private int sixes;

private double strikeRate;

private boolean out;
private String dismissalType;

public Long getBatterId() {
    return batterId;
}

public void setBatterId(Long batterId) {
    this.batterId = batterId;
}

public String getBatterName() {
    return batterName;
}

public void setBatterName(String batterName) {
    this.batterName = batterName;
}

public int getRuns() {
    return runs;
}

public void setRuns(int runs) {
    this.runs = runs;
}

public int getBallsFaced() {
    return ballsFaced;
}

public void setBallsFaced(int ballsFaced) {
    this.ballsFaced = ballsFaced;
}

public int getFours() {
    return fours;
}

public void setFours(int fours) {
    this.fours = fours;
}

public int getSixes() {
    return sixes;
}

public void setSixes(int sixes) {
    this.sixes = sixes;
}

public double getStrikeRate() {
    return strikeRate;
}

public void setStrikeRate(double strikeRate) {
    this.strikeRate = strikeRate;
}

public boolean isOut() {
    return out;
}

public void setOut(boolean out) {
    this.out = out;
}

public String getDismissalType() {
    return dismissalType;
}

public void setDismissalType(String dismissalType) {
    this.dismissalType = dismissalType;
}


}
