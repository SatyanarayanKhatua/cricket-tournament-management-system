package com.cricket.tournament.dto;

public class BallDeliveryResponse {

    private Long id;
    private Long inningsId;
    private int deliveryNumber;

    private Long strikerId;
    private String strikerName;

    private Long nonStrikerId;
    private String nonStrikerName;

    private Long bowlerId;
    private String bowlerName;

    private int batRuns;
    private int wideRuns;
    private int noBallRuns;
    private int byeRuns;
    private int legByeRuns;
    private int penaltyRuns;

    private int totalExtras;
    private int totalRuns;

    private boolean legalDelivery;
    private boolean wicket;

    private String dismissalType;

    private Long dismissedPlayerId;
    private String dismissedPlayerName;

    private Long fielderId;
    private String fielderName;
    
    private Boolean battersCrossed;
    
    private String runOutEnd;
    
    private boolean overFinished;

    private boolean bowlerSelectionRequired;

    private String commentary;

    public BallDeliveryResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInningsId() {
        return inningsId;
    }

    public void setInningsId(Long inningsId) {
        this.inningsId = inningsId;
    }

    public int getDeliveryNumber() {
        return deliveryNumber;
    }

    public void setDeliveryNumber(int deliveryNumber) {
        this.deliveryNumber = deliveryNumber;
    }

    public Long getStrikerId() {
        return strikerId;
    }

    public void setStrikerId(Long strikerId) {
        this.strikerId = strikerId;
    }

    public String getStrikerName() {
        return strikerName;
    }

    public void setStrikerName(String strikerName) {
        this.strikerName = strikerName;
    }

    public Long getNonStrikerId() {
        return nonStrikerId;
    }

    public void setNonStrikerId(Long nonStrikerId) {
        this.nonStrikerId = nonStrikerId;
    }

    public String getNonStrikerName() {
        return nonStrikerName;
    }

    public void setNonStrikerName(String nonStrikerName) {
        this.nonStrikerName = nonStrikerName;
    }

    public Long getBowlerId() {
        return bowlerId;
    }

    public void setBowlerId(Long bowlerId) {
        this.bowlerId = bowlerId;
    }

    public String getBowlerName() {
        return bowlerName;
    }

    public void setBowlerName(String bowlerName) {
        this.bowlerName = bowlerName;
    }

    public int getBatRuns() {
        return batRuns;
    }

    public void setBatRuns(int batRuns) {
        this.batRuns = batRuns;
    }

    public int getWideRuns() {
        return wideRuns;
    }

    public void setWideRuns(int wideRuns) {
        this.wideRuns = wideRuns;
    }

    public int getNoBallRuns() {
        return noBallRuns;
    }

    public void setNoBallRuns(int noBallRuns) {
        this.noBallRuns = noBallRuns;
    }

    public int getByeRuns() {
        return byeRuns;
    }

    public void setByeRuns(int byeRuns) {
        this.byeRuns = byeRuns;
    }

    public int getLegByeRuns() {
        return legByeRuns;
    }

    public void setLegByeRuns(int legByeRuns) {
        this.legByeRuns = legByeRuns;
    }

    public int getPenaltyRuns() {
        return penaltyRuns;
    }

    public void setPenaltyRuns(int penaltyRuns) {
        this.penaltyRuns = penaltyRuns;
    }

    public int getTotalExtras() {
        return totalExtras;
    }

    public void setTotalExtras(int totalExtras) {
        this.totalExtras = totalExtras;
    }

    public int getTotalRuns() {
        return totalRuns;
    }

    public void setTotalRuns(int totalRuns) {
        this.totalRuns = totalRuns;
    }

    public boolean isLegalDelivery() {
        return legalDelivery;
    }

    public void setLegalDelivery(boolean legalDelivery) {
        this.legalDelivery = legalDelivery;
    }

    public boolean isWicket() {
        return wicket;
    }

    public void setWicket(boolean wicket) {
        this.wicket = wicket;
    }

    public String getDismissalType() {
        return dismissalType;
    }

    public void setDismissalType(String dismissalType) {
        this.dismissalType = dismissalType;
    }

    public Long getDismissedPlayerId() {
        return dismissedPlayerId;
    }

    public void setDismissedPlayerId(Long dismissedPlayerId) {
        this.dismissedPlayerId = dismissedPlayerId;
    }

    public String getDismissedPlayerName() {
        return dismissedPlayerName;
    }

    public void setDismissedPlayerName(String dismissedPlayerName) {
        this.dismissedPlayerName = dismissedPlayerName;
    }

    public Long getFielderId() {
        return fielderId;
    }

    public void setFielderId(Long fielderId) {
        this.fielderId = fielderId;
    }

    public String getFielderName() {
        return fielderName;
    }

    public void setFielderName(String fielderName) {
        this.fielderName = fielderName;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }
    
    public Boolean getBattersCrossed() {
        return battersCrossed;
    }

    public void setBattersCrossed(Boolean battersCrossed) {
        this.battersCrossed = battersCrossed;
    }
    public String getRunOutEnd() {
        return runOutEnd;
    }

    public void setRunOutEnd(String runOutEnd) {
        this.runOutEnd = runOutEnd;
    }
    
    public boolean isOverFinished() {
        return overFinished;
    }

    public void setOverFinished(boolean overFinished) {
        this.overFinished = overFinished;
    }

    public boolean isBowlerSelectionRequired() {
        return bowlerSelectionRequired;
    }

    public void setBowlerSelectionRequired(boolean bowlerSelectionRequired) {
        this.bowlerSelectionRequired = bowlerSelectionRequired;
    }
}