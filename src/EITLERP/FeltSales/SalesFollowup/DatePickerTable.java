/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.SalesFollowup;

import EITLERP.data;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DateFormatter;

public class DatePickerTable extends JTable {

    public static String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
    public static final int DIALOG_WIDTH = 360;
    public static final int DIALOG_HEIGHT = 210;

    public SimpleDateFormat dateFormat;
    public DatePanel datePanel = null;
    public JDialog dateDialog = null;
    public int selerow = 0, selecolumn = 0, posx, posy;
    public JTable tbl;
    public JSpinner.DateEditor editor;
    public JSpinner spinner;
    public boolean closeflag = false;

    public DatePickerTable() {
        this(new Date());
    }

//    public DatePickerTable(int prow, int pcol, JTable ptbl, int pposx, int pposy) {
//        this(null, prow, pcol, ptbl, pposx, pposy);
//    }
    public DatePickerTable(String dateFormatPattern, Date date) {
        this(date);
        DEFAULT_DATE_FORMAT = dateFormatPattern;
    }

    public DatePickerTable(Date date) {
//        setDate(date);
//        setEditable(false);
//        setCursor(new Cursor(Cursor.HAND_CURSOR));
//        addListeners();
    }

    public DatePickerTable(Date date, int Prow, int Pcol, JTable ptbl, int pposx, int pposy, String psdate, String pedate) {
        this.selerow = Prow;
        this.selecolumn = Pcol;
        this.posx = pposx;
        this.posy = pposy;
        this.tbl = ptbl;
        setDate(date);
        
        if (isEnabled()) {
            if (datePanel == null) {
                //datePanel = new DatePanel();
                datePanel = new DatePanel(psdate, pedate);
            }
            //Point point = getLocationOnScreen();
            Point point = new Point(posx, posy);
            //point.x=0;
            //point.y = point.y + 30;
            //point.y =  30;
            showDateDialog(datePanel, point);
        }
        //setEditable(false);
//        setCursor(new Cursor(Cursor.HAND_CURSOR));
//        addListeners();
    }

//    public void addListeners() {
//        addMouseListener(new MouseAdapter() {
//            public void mouseClicked(MouseEvent paramMouseEvent) {
//                if (isEnabled()) {
//                    if (datePanel == null) {
//                        datePanel = new DatePanel();
//                    }
//                    Point point = getLocationOnScreen();
//                    point.y = point.y + 30;
//                    showDateDialog(datePanel, point);
//                }
//            }
//        });
//    }
    public void showDateDialog(DatePanel dateChooser, Point position) {
//        Frame owner = (Frame) SwingUtilities
//                .getWindowAncestor(DatePickerTable.this);

        JFrame owner = new JFrame();
        if (dateDialog == null || dateDialog.getOwner() != owner) {
            dateDialog = createDateDialog(owner, dateChooser);
        }

        dateDialog.setLocation(getAppropriateLocation(owner, position));
        dateDialog.setVisible(true);
        

    }

    public JDialog createDateDialog(Frame owner, JPanel contentPanel) {
        JDialog dialog = new JDialog(owner, "Date Selected", true);
        dialog.setUndecorated(true);
        dialog.getContentPane().add(contentPanel, BorderLayout.CENTER);

        dialog.pack();
        dialog.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        return dialog;
    }

    public Point getAppropriateLocation(Frame owner, Point position) {
        Point result = new Point(position);
        Point p = owner.getLocation();
        int offsetX = (position.x + DIALOG_WIDTH) - (p.x + owner.getWidth());
        int offsetY = (position.y + DIALOG_HEIGHT) - (p.y + owner.getHeight());

//        if (offsetX > 0) {
//            result.x -= offsetX;
//        }
//
//        if (offsetY > 0) {
//            result.y -= offsetY;
//        }
        return result;
    }

    public SimpleDateFormat getDefaultDateFormat() {
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        }
        return dateFormat;
    }

    public void setText(Date date) {
        setDate(date);
    }

    public void setDate(Date date) {
//        super.setText(getDefaultDateFormat().format(date));
//        super.setValueAt(getDefaultDateFormat().format(date), selerow,selecolumn);
        try {
            if (!closeflag) {
                tbl.setValueAt(getDefaultDateFormat().format(date), selerow, selecolumn);
                tbl.setValueAt(spinner.getValue().toString().substring(11, 16), selerow, selecolumn + 1);
            }
            
        } catch (Exception e) {

        }
    }

    public Date getDate() {
        try {
            //return getDefaultDateFormat().parse(getText());
            return getDefaultDateFormat().parse(tbl.getValueAt(selerow, selecolumn).toString());
        } catch (ParseException e) {
            return new Date();
        }
    }

    public class DatePanel extends JPanel implements ChangeListener {

        int startYear = 1920;
        int lastYear = 2050;

        Color backGroundColor = Color.gray;
        Color palletTableColor = Color.white;
        Color todayBackColor = Color.MAGENTA;
        Color weekFontColor = Color.blue;
        Color dateFontColor = Color.black;
        Color weekendFontColor = Color.red;

        Color controlLineColor = Color.pink;
        Color controlTextColor = Color.white;

        JSpinner yearSpin;
        JSpinner monthSpin;
        JButton[][] daysButton = new JButton[6][7];
        JButton close;

        String stdate, enddate;

        DatePanel() {
            setLayout(new BorderLayout());
            setBorder(new LineBorder(backGroundColor, 2));
            setBackground(backGroundColor);

            JPanel topYearAndMonth = createYearAndMonthPanal();
            add(topYearAndMonth, BorderLayout.NORTH);

            JPanel centerWeekAndDay = createWeekAndDayPanal();
            add(centerWeekAndDay, BorderLayout.CENTER);

            reflushWeekAndDay();

        }

        DatePanel(String pstdate, String penddate) {
            stdate = pstdate;
            enddate = penddate;

            setLayout(new BorderLayout());
            setBorder(new LineBorder(backGroundColor, 2));
            setBackground(backGroundColor);

            JPanel topYearAndMonth = createYearAndMonthPanal();
            add(topYearAndMonth, BorderLayout.NORTH);

            JPanel centerWeekAndDay = createWeekAndDayPanal();
            add(centerWeekAndDay, BorderLayout.CENTER);

            reflushWeekAndDay(pstdate, penddate);

        }

        public JPanel createYearAndMonthPanal() {
            Calendar cal = getCalendar();
            int currentYear = cal.get(Calendar.YEAR);
            int currentMonth = cal.get(Calendar.MONTH) + 1;

            

            JPanel panel = new JPanel();
            panel.setLayout(new FlowLayout());
            panel.setBackground(controlLineColor);

            try {
                yearSpin = new JSpinner(new SpinnerNumberModel(currentYear,
                        Integer.parseInt(stdate.substring(0, 4)), Integer.parseInt(enddate.substring(0, 4)), 1));
            } catch (Exception e) {
                yearSpin = new JSpinner(new SpinnerNumberModel(currentYear,
                        startYear, lastYear, 1));
            }
            yearSpin.setPreferredSize(new Dimension(56, 20));
            yearSpin.setName("Year");
            yearSpin.setEditor(new JSpinner.NumberEditor(yearSpin, "####"));
            yearSpin.addChangeListener(this);
            panel.add(yearSpin);

            JLabel yearLabel = new JLabel("Year");
            yearLabel.setForeground(controlTextColor);
            panel.add(yearLabel);

            monthSpin = new JSpinner(new SpinnerNumberModel(currentMonth, 1,
                    12, 1));
            monthSpin.setPreferredSize(new Dimension(35, 20));
            monthSpin.setName("Month");
            monthSpin.addChangeListener(this);
            panel.add(monthSpin);

            JLabel monthLabel = new JLabel("Month");
            monthLabel.setForeground(controlTextColor);
            panel.add(monthLabel);

            Calendar calendar = Calendar.getInstance();
            try {
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tbl.getValueAt(selerow, selecolumn + 1).toString().substring(0, 2))); // 24 == 12 PM == 00:00:00
                calendar.set(Calendar.MINUTE, Integer.parseInt(tbl.getValueAt(selerow, selecolumn + 1).toString().substring(3, 5)));
                calendar.set(Calendar.SECOND, 0);
            } catch (Exception e) {
                calendar.set(Calendar.HOUR_OF_DAY, 12); // 24 == 12 PM == 00:00:00
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
            }
            SpinnerDateModel model = new SpinnerDateModel();
            model.setValue(calendar.getTime());

            spinner = new JSpinner(model);

            editor = new JSpinner.DateEditor(spinner, "hh:mm a");
            DateFormatter formatter = (DateFormatter) editor.getTextField().getFormatter();
            formatter.setAllowsInvalid(false); // this makes what you want
            formatter.setOverwriteMode(true);
            spinner.setEditor(editor);
            
            JPanel content = new JPanel();
            content.add(spinner);
            spinner.setVisible(false);
            panel.add(content);

            close = new JButton("X");
            close.setPreferredSize(new Dimension(60, 20));
            close.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    closeflag = true;
                    dateDialog.setVisible(false);
                }
            });
            panel.add(close);

            return panel;
        }

        public JPanel createWeekAndDayPanal() {
            String colname[] = {"S", "M", "T", "W", "T", "F", "S"};
            JPanel panel = new JPanel();
            panel.setFont(new Font("Arial", Font.PLAIN, 10));
            panel.setLayout(new GridLayout(7, 7));
            panel.setBackground(Color.white);

            for (int i = 0; i < 7; i++) {
                JLabel cell = new JLabel(colname[i]);
                cell.setHorizontalAlignment(JLabel.RIGHT);
                cell.setHorizontalAlignment(JTextField.CENTER);
                if (i == 0) {
                    cell.setForeground(weekendFontColor);
                } else {
                    cell.setForeground(weekFontColor);
                }
                panel.add(cell);
            }

            int actionCommandId = 0;
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 7; j++) {
                    JButton numBtn = new JButton();
                    numBtn.setBorder(null);
                    numBtn.setHorizontalAlignment(SwingConstants.RIGHT);
                    numBtn.setActionCommand(String
                            .valueOf(actionCommandId));
                    numBtn.setBackground(palletTableColor);
                    numBtn.setForeground(dateFontColor);
                    numBtn.setFont(new Font("Arial Black", Font.BOLD, 16));
                    numBtn.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent event) {
                            JButton source = (JButton) event.getSource();
                            if (source.getText().length() == 0) {
                                return;
                            }
                            dayColorUpdate(true);
                            source.setForeground(todayBackColor);
                            int newDay = Integer.parseInt(source.getText());
                            Calendar cal = getCalendar();
                            cal.set(Calendar.DAY_OF_MONTH, newDay);
                            setDate(cal.getTime());

                            dateDialog.setVisible(false);
                        }
                    });

                    if (j == 0) {
                        numBtn.setForeground(weekendFontColor);
                    } else {
                        numBtn.setForeground(dateFontColor);
                    }
                    daysButton[i][j] = numBtn;
                    daysButton[i][j].setHorizontalAlignment(JTextField.CENTER);
                    panel.add(numBtn);
                    actionCommandId++;
                }
            }

            return panel;
        }

        public Calendar getCalendar() {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(getDate());
            return calendar;
        }

        public int getSelectedYear() {
            return ((Integer) yearSpin.getValue()).intValue();
        }

        public int getSelectedMonth() {
            return ((Integer) monthSpin.getValue()).intValue();
        }

        public void dayColorUpdate(boolean isOldDay) {
            Calendar cal = getCalendar();
            int day = cal.get(Calendar.DAY_OF_MONTH);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            int actionCommandId = day - 2 + cal.get(Calendar.DAY_OF_WEEK);
            int i = actionCommandId / 7;
            int j = actionCommandId % 7;
            if (isOldDay) {
                daysButton[i][j].setForeground(dateFontColor);
            } else {
                daysButton[i][j].setForeground(todayBackColor);
            }
        }

        public void reflushWeekAndDay() {
            try {
                Calendar cal = getCalendar();
                cal.set(Calendar.DAY_OF_MONTH, 1);

                int maxDayNo = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                int dayNo = 2 - cal.get(Calendar.DAY_OF_WEEK);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date1 = sdf.parse(data.getStringValueFromDB("SELECT STAFF_APL_DATE FROM SDMLATTPAY.ATT_PROCESS_LOCK_DATE"));
                Date date2;
                for (int i = 0; i < 6; i++) {
                    for (int j = 0; j < 7; j++) {
                        String s = "";
                        if (dayNo >= 1 && dayNo <= maxDayNo) {
                            s = String.valueOf(dayNo);
                        }
                        daysButton[i][j].setText(s);
                        dayNo++;
                    }
                }
                dayColorUpdate(false);
            } catch (Exception e) {

            }
        }

        public void reflushWeekAndDay(String pstdt, String pedt) {
            try {
                Calendar cal = getCalendar();
                cal.set(Calendar.DAY_OF_MONTH, 1);

                int maxDayNo = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                int dayNo = 2 - cal.get(Calendar.DAY_OF_WEEK);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date1 = sdf.parse(pstdt);
                Date date3 = sdf.parse(pedt);
                Date date2;

                for (int i = 0; i < 6; i++) {
                    for (int j = 0; j < 7; j++) {
                        String s = "";
                        if (dayNo >= 1 && dayNo <= maxDayNo) {
                            date2 = sdf.parse(yearSpin.getValue().toString() + "-" + monthSpin.getValue().toString() + "-" + dayNo);
                            if (date2.compareTo(date1) >= 0 && date3.compareTo(date2) >= 0) {
                                s = String.valueOf(dayNo);
                            }
                        }
                        daysButton[i][j].setText(s);
                        dayNo++;
                    }
                }
                dayColorUpdate(false);
            } catch (Exception e) {

            }
        }

        public void stateChanged(ChangeEvent e) {
            dayColorUpdate(true);

            JSpinner source = (JSpinner) e.getSource();
            Calendar cal = getCalendar();
            if (source.getName().equals("Year")) {
                cal.set(Calendar.YEAR, getSelectedYear());
            } else {
                cal.set(Calendar.MONTH, getSelectedMonth() - 1);
            }
            setDate(cal.getTime());
            reflushWeekAndDay(stdate, enddate);
        }
    }
}
