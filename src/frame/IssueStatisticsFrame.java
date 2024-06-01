package frame;

import util.RestClient_Get;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("serial")
public class IssueStatisticsFrame extends JFrame {
    private long projectId;
    private int year;

    public IssueStatisticsFrame(long projectId, int year) {
        this.projectId = projectId;
        this.year = year;

        setTitle("Issue Statistics");
        setSize(1200, 800);
        setLocationRelativeTo(null); // 화면 중앙 위치
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        loadStatistics();
        setVisible(true);
    }

    private void loadStatistics() {
        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                String url = String.format("http://localhost:8080/issues/statistic/%d?year=%d", projectId, year);
                return RestClient_Get.sendGetRequest(url);
            }

            @Override
            protected void done() {
                try {
                    String response = get();
                    System.out.println("Response from server: " + response);

                    JSONObject jsonResponse = new JSONObject(response);
                    boolean isSuccess = jsonResponse.getBoolean("isSuccess");
                    String code = jsonResponse.getString("code");

                    if (isSuccess && "ISSUE_3000".equals(code)) {
                        JSONObject resultObject = jsonResponse.getJSONObject("result");
                        JSONArray issueCountArray = resultObject.getJSONArray("issue_count");

                        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

                        for (int i = 0; i < issueCountArray.length(); i++) {
                            JSONObject issueCountObject = issueCountArray.getJSONObject(i);
                            int month = issueCountObject.getInt("month");
                            int blocker = issueCountObject.getInt("blocker");
                            int critical = issueCountObject.getInt("critical");
                            int major = issueCountObject.getInt("major");
                            int minor = issueCountObject.getInt("minor");
                            int trivial = issueCountObject.getInt("trivial");

                            dataset.addValue(blocker, "Blocker", String.valueOf(month));
                            dataset.addValue(critical, "Critical", String.valueOf(month));
                            dataset.addValue(major, "Major", String.valueOf(month));
                            dataset.addValue(minor, "Minor", String.valueOf(month));
                            dataset.addValue(trivial, "Trivial", String.valueOf(month));
                        }

                        JFreeChart barChart = ChartFactory.createBarChart(
                                "Issue Statistics",
                                "Month",
                                "Number of Issues",
                                dataset
                        );

                        CategoryPlot plot = (CategoryPlot) barChart.getPlot();
                        BarRenderer renderer = (BarRenderer) plot.getRenderer();
                        renderer.setDrawBarOutline(false);
                        
                        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
                        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

                        ChartPanel chartPanel = new ChartPanel(barChart);
                        chartPanel.setBounds(50, 200, 1100, 500);

                        getContentPane().removeAll();
                        add(chartPanel);
                        revalidate();
                        repaint();
                    } else {
                        String message = jsonResponse.getString("message");
                        JOptionPane.showMessageDialog(IssueStatisticsFrame.this, "Failed to load statistics: " + message, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(IssueStatisticsFrame.this, "Failed to load statistics: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }
}
