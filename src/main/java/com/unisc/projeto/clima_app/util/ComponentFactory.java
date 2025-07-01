package com.unisc.projeto.clima_app.util;

import java.awt.Component;
import java.awt.Font;
import java.awt.LayoutManager;
import java.time.LocalDate;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.unisc.projeto.clima_app.view.CardClima;

public class ComponentFactory {

	private static final Font FONT_BOLD_14 = new Font("Segoe UI", Font.BOLD, 14);
	private static final Font FONT_PLAIN_14 = new Font("Segoe UI", Font.PLAIN, 14);

	public static JLabel createLabel(String text) {
		return createLabel(text, FONT_PLAIN_14);
	}
	
	public static JLabel createLabel(String text, Font font) {
		JLabel label = new JLabel(text);
		label.setFont(font);
		return label;
	}

	public static JButton createButton(String text) {
		return createButton(text, null);
	}

	public static JButton createButton(ImageIcon icon) {
		return createButton(null, icon);
	}

	public static JButton createButton(String text, ImageIcon icon) {
		JButton button = new JButton(text);
		button.setIcon(icon);
		button.setFont(FONT_PLAIN_14);
		return button;
	}

	public static JTextField createTextField(int columns) {
		JTextField textField = new JTextField(columns);
		textField.setFont(FONT_PLAIN_14);
		return textField;
	}

	public static JPanel createPanel() {
		return new JPanel();
	}

	public static JPanel createPanel(LayoutManager layout) {
		return new JPanel(layout);
	}

	public static JTable createTable(String[] columnHeaders) {
		JTable table = new JTable();
		table.setFont(FONT_PLAIN_14);
		table.setRowHeight(28);
		table.getTableHeader().setFont(FONT_BOLD_14);
		table.setFillsViewportHeight(true);

		DefaultTableModel tableModel = new DefaultTableModel(new Object[][] {}, columnHeaders) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.setModel(tableModel);
		//pra colocar o icone na tabela
		table.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				
				if (value instanceof Object[]) {
					Object[] cellData = (Object[]) value;
					setIcon((ImageIcon) cellData[0]);
					setText((String) cellData[1]);
					setHorizontalAlignment(SwingConstants.CENTER); 
					setIconTextGap(5); 
				} else {
					setIcon(null);
					setText(value != null ? value.toString() : "");
					setHorizontalAlignment(SwingConstants.CENTER);
				}
				return this;
			}
		});
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		table.setDefaultRenderer(Object.class, centerRenderer);

		return table;
	}

	public static JMenuBar createMenuBar() {
		return new JMenuBar();
	}

	public static JMenu createMenu(String text) {
		JMenu menu = new JMenu(text);
		menu.setFont(FONT_PLAIN_14);
		return menu;
	}

	public static JMenuItem createMenuItem(String text) {
		JMenuItem menuItem = new JMenuItem(text);
		menuItem.setFont(FONT_PLAIN_14);
		return menuItem;
	}

	public static DatePicker createDatePicker() {
		return createDatePicker(null);
	}

	public static DatePicker createDatePicker(LocalDate date) {
		DatePickerSettings settings = new DatePickerSettings();
		settings.setFormatForDatesCommonEra("dd/MM/yyyy");
		DatePicker datePicker = new DatePicker(settings);
		datePicker.setDate(date);
		return datePicker;
	}

	public static <E> JComboBox<E> createCombobox(E[] itens) {
		JComboBox<E> combobox = new JComboBox<E>(itens);
		combobox.setFont(FONT_PLAIN_14);
		return combobox;
	}
	
	public static JTextArea createTextArea() {
		return createTextArea(10, 40, false, true);
	}
	
	public static JTextArea createTextArea(int rows, int columns, boolean editable, boolean lineWrap) {
		JTextArea txtArea = new JTextArea();
		txtArea.setFont(FONT_PLAIN_14);
		txtArea.setEditable(editable);
		txtArea.setRows(rows);
		txtArea.setColumns(columns);
		txtArea.setLineWrap(lineWrap);
		txtArea.setWrapStyleWord(true);
		return txtArea;
	}
	
	public static CardClima createCardClima(String title, String value, String subtext, ImageIcon icon) {
		return new CardClima(title, value, subtext, icon);
	}
}