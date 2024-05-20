package style;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.DropMode;

public class ProjStyleTable extends JTable {

    private TableDarkHeader header;
    private TableDarkCell cell;

    public ProjStyleTable() {
        header = new TableDarkHeader();
        cell = new TableDarkCell();
        
        getTableHeader().setDefaultRenderer(header);
        getTableHeader().setPreferredSize(new Dimension(0, 35)); // header 높이
        
        setDefaultRenderer(Object.class, cell);
        getTableHeader().setReorderingAllowed(false); // 테이블 드래그앤드롭 금지
        setIntercellSpacing(new Dimension(0, 0)); // 테이블 셀 사이 틈 없애기
        setRowHeight(30); // cell 높이
    }
    
    public void setColumnAlignment(int column, int align) {	// header 좌/우/중앙 정렬
        header.setAlignment(column, align);
    }

    public void setCellAlignment(int column, int align) {	// cell 좌/우/중앙 정렬
        cell.setAlignment(column, align);
    }
	
    public void setColumnWidth(int column, int width) {		// 각 cell width 설정
        getColumnModel().getColumn(column).setPreferredWidth(width);
        getColumnModel().getColumn(column).setMinWidth(width);
        getColumnModel().getColumn(column).setMaxWidth(width);
    }
	
    public void fixTable(JScrollPane scroll) {
        scroll.setVerticalScrollBar(new ProjStyleScrollBar());
        JPanel panel = new JPanel();
        panel.setBackground(ProjColor.customGray);
        scroll.setCorner(JScrollPane.UPPER_RIGHT_CORNER, panel);
        scroll.getViewport().setBackground(ProjColor.customGray);
        scroll.setBorder(BorderFactory.createLineBorder(ProjColor.customGray, 2));
    }

    private class TableDarkHeader extends DefaultTableCellRenderer {

    	
        private Map<Integer, Integer> alignment = new HashMap<>();

        public void setAlignment(int column, int align) {
            alignment.put(column, align);
        }
		
        @Override
        public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
            Component com = super.getTableCellRendererComponent(jtable, o, bln, bln1, i, i1);
            com.setBackground(ProjColor.tableHeaderGray);
            com.setForeground(Color.WHITE);
            com.setFont(com.getFont().deriveFont(Font.BOLD, 14));
            
            if (alignment.containsKey(i1)) {
                setHorizontalAlignment(alignment.get(i1));
            } else {
                setHorizontalAlignment(JLabel.LEFT);
            }
            
            return com;
        }
    }

    private class TableDarkCell extends DefaultTableCellRenderer {
    	
        private Map<Integer, Integer> alignment = new HashMap<>();

        public void setAlignment(int column, int align) {
            alignment.put(column, align);
        }
		
        @Override
        public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int row, int column) {
            Component com = super.getTableCellRendererComponent(jtable, o, bln, bln1, row, column);
            com.setForeground(Color.BLACK);
            com.setFont(com.getFont().deriveFont(Font.PLAIN, 14));
            
            if (isCellSelected(row, column)) { // 선택한 row, column 색깔
                if (row % 2 == 0) { // row가 짝수일때
                    com.setBackground(ProjColor.selectedTableCustomGray); // 컴포넌트의 백그라운드 색깔 변경
                    com.setForeground(Color.WHITE);
                } else {
                	com.setBackground(ProjColor.selectedTableAnotherCellGray);
                    com.setForeground(Color.WHITE);
                }
            } else { // default 색깔
                if (row % 2 == 0) {
                    com.setBackground(ProjColor.tableAnotherCellGray);
                } else {
                    com.setBackground(ProjColor.customGray);
                }
            }
            
            setBorder(new EmptyBorder(0, 5, 0, 5));
            
            if (alignment.containsKey(column)) {
                setHorizontalAlignment(alignment.get(column));
            } else {
                setHorizontalAlignment(JLabel.LEFT);
            }
            
            return com;
        }
    }
}