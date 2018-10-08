package ru.avb.iremember;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import org.joda.time.DateTime;

import java.util.ArrayList;

public class Category {
    int id = NO_SPECIFIED;
    String label;
    int condition = Condition.NOTSELECTED;
    int iconId = NO_SPECIFIED;
    int parentCategory = NO_SPECIFIED;
    int orderNumber = NO_SPECIFIED;
    DateTime dateCreated;
    DateTime dateModified;
    long initialValue = NO_SPECIFIED;
    long finalValue = NO_SPECIFIED;
    boolean finalValueEnabled;
    boolean negativeValueEnabled;
    boolean predictionEnabled;
    int predictionPeriod = NO_SPECIFIED;
    int everageValue =NO_SPECIFIED;
    boolean everageValueCalculateEnabled;
    int everageValueCalculateEventcount = NO_SPECIFIED;
    boolean favoriteEnabled;
    int favoriteOrderNumber =NO_SPECIFIED;
    String unitLabel = "";

    public static final class dateTimeFormat{
        public static final String date = "dd.MM.yyyy";
        public static final String dateTime = "dd.MM.yyyy  HH:mm";
    }
    public static final int NO_CATEGORY = -1999999999, NO_SPECIFIED = -1999999998;

    public static final class Condition{
        public static final int NOTSELECTED=0, UNIT=1, DATE=2, DATETIME=3;
        public static final String[] labels = {"Not selected", "unit", "date", "date time"};
    }

    public static final class Prediction{
        public static final int[] period = {1,2,3,4,5,6,7,14,21,30};
    }

    //GETTER SETTER
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public int getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(int parentCategory) {
        this.parentCategory = parentCategory;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public DateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(DateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public DateTime getDateModified() {
        return dateModified;
    }

    public void setDateModified(DateTime dateModified) {
        this.dateModified = dateModified;
    }

    public long getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(long initialValue) {
        this.initialValue = initialValue;
    }

    public long getFinalValue() {
        return finalValue;
    }

    public void setFinalValue(long finalValue) {
        this.finalValue = finalValue;
    }

    public boolean isFinalValueEnabled() {
        return finalValueEnabled;
    }

    public void setFinalValueEnabled(boolean finalValueEnabled) {
        this.finalValueEnabled = finalValueEnabled;
    }

    public boolean isNegativeValueEnabled() {
        return negativeValueEnabled;
    }

    public void setNegativeValueEnabled(boolean negativeValueEnabled) {
        this.negativeValueEnabled = negativeValueEnabled;
    }

    public boolean isPredictionEnabled() {
        return predictionEnabled;
    }

    public void setPredictionEnabled(boolean predictionEnabled) {
        this.predictionEnabled = predictionEnabled;
    }

    public int getPredictionPeriod() {
        return predictionPeriod;
    }

    public void setPredictionPeriod(int predictionPeriod) {
        this.predictionPeriod = predictionPeriod;
    }

    public int getEverageValue() {
        return everageValue;
    }

    public void setEverageValue(int everageValue) {
        this.everageValue = everageValue;
    }

    public boolean isEverageValueCalculateEnabled() {
        return everageValueCalculateEnabled;
    }

    public void setEverageValueCalculateEnabled(boolean everageValueCalculateEnabled) {
        this.everageValueCalculateEnabled = everageValueCalculateEnabled;
    }

    public int getEverageValueCalculateEventcount() {
        return everageValueCalculateEventcount;
    }

    public void setEverageValueCalculateEventcount(int everageValueCalculateEventcount) {
        this.everageValueCalculateEventcount = everageValueCalculateEventcount;
    }

    public boolean isFavoriteEnabled() {
        return favoriteEnabled;
    }

    public void setFavoriteEnabled(boolean favoriteEnabled) {
        this.favoriteEnabled = favoriteEnabled;
    }

    public int getFavoriteOrderNumber() {
        return favoriteOrderNumber;
    }

    public void setFavoriteOrderNumber(int favoriteOrderNumber) {
        this.favoriteOrderNumber = favoriteOrderNumber;
    }

    public String getUnitLabel() {
        return unitLabel;
    }

    public void setUnitLabel(String unitLabel) {
        this.unitLabel = unitLabel;
    }
}
