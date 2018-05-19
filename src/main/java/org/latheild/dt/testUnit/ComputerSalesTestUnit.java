package org.latheild.dt.testUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ComputerSalesTestUnit {

    private static Logger logger = LoggerFactory.getLogger(ComputerSalesTestUnit.class);

    private final static int _maxMainframeNum = 70;

    private final static int _minMainframeNum = 1;

    private final static int _maxMonitorNum = 80;

    private final static int _minMonitorNum = 1;

    private final static int _maxPeripheralNum = 80;

    private final static int _minPeripheralNum = 1;

    private final static int _mainframePrice = 25;

    private final static int _monitorPrice = 30;

    private final static int _peripheralPrice = 45;

    private static int _mainframeNum;

    private static int _monitorNum;

    private static int _peripheralNum;

    public ComputerSalesTestUnit() {
        //System.out.println(this.getClass() + " created.");
        logger.info("Class created " + this.getClass());
    }

    private static void checkInput(String mainframeNum, String monitorNum, String peripheralNum) throws Exception {
        int mainframe = Integer.parseInt(mainframeNum);
        int monitor = Integer.parseInt(monitorNum);
        int peripheral = Integer.parseInt(peripheralNum);

        if (_minMainframeNum >= mainframe || mainframe >= _maxMainframeNum) {
            throw new Exception("Input number for mainframe is out of range.");
        } else if (_minMonitorNum >= monitor || monitor >= _maxMonitorNum) {
            throw new Exception("Input number for monitor is out of range.");
        } else if (_minPeripheralNum >= peripheral || peripheral >= _maxPeripheralNum) {
            throw new Exception("Input number for peripheral is out of range.");
        } else {
            _mainframeNum = mainframe;
            _monitorNum = monitor;
            _peripheralNum = peripheral;
        }
    }

    public static double getCommission(String mainframeNum, String monitorNum, String peripheralNum) throws Exception {
        try {
            checkInput(mainframeNum, monitorNum, peripheralNum);
        } catch (NumberFormatException e) {
            logger.error("Input data is not a valid number.");
            throw new NumberFormatException("Input data is not a valid number.");
            //return scale(-1.0);
        } catch (Exception e) {
            logger.error(e.getMessage());
            System.out.println(e.getMessage());
            throw new Exception(e.getMessage());
            //System.out.println("AssertionError" + e.getMessage());
            //return scale(-2.0);
        }

        logger.debug("Commission successfully calculated.");
        return scale(getCommission());
    }

    private static double getCommission() {
        int grossValue = (_mainframeNum * _mainframePrice)
                + (_monitorNum * _monitorPrice)
                + (_peripheralNum * _peripheralPrice);
        double commission = 0;

        if (grossValue > 1800) {
            commission = grossValue * 0.2;
        } else if (grossValue > 1000) {
            commission = grossValue * 0.15;
        } else {
            commission = grossValue * 0.1;
        }

        return commission;
    }

    private static double scale(double origin) {
        return BigDecimal.valueOf(origin).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
