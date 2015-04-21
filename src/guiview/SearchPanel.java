package guiview;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.model.Tag;
import cs213.photoAlbum.util.DateUtils;

/**
 * Search screen.
 * Shows list of photos satisfying criteria on one side.
 * Panel to specify search parameters is on the other side
 * 
 * @author Ananth Rao
 * @author Allon Fineziber
 *
 */
public class SearchPanel extends JPanel implements ActionListener, ListSelectionListener {
	public MainApp parent;
	
	public JRadioButton byDate;
	public JRadioButton byTag;
	
	public JList<Tag> tags;
	public DefaultListModel<Tag> listModel;
	public JButton removeTagBtn;

	public JTextField type;
	public JTextField value;
	public JButton addTagBtn;
	
	public JTextField year1;
	public JTextField month1;
	public JTextField day1;
	public JTextField hour1;
	public JTextField minute1;
	public JTextField second1;

	public JTextField year2;
	public JTextField month2;
	public JTextField day2;
	public JTextField hour2;
	public JTextField minute2;
	public JTextField second2;
	
	public JTabbedPane searchPane;
	public JPanel searchPanel;
	public final String toTag = "TAG";
	public final String toDate = "DATE";
	
	public JList<Photo> photos;
	public DefaultListModel<Photo> photoModel;
	
	public JButton searchBtn;
	
	public JButton backBtn;
	
	public JButton createAlbumBtn;
	
	public StatusBar status;
	
	public SearchPanel(MainApp parent) {
		super();
		this.setLayout(new BorderLayout());
		this.parent = parent;
		setupPanels();
	}
	
/**
 * Central method for setting up all parts of the UI in this screen
 * 
 * @author Ananth Rao
 * @author Allon Fineziber
 * 
 * 
 */
	public void setupPanels() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(createMainPanel(), BorderLayout.CENTER);
		panel.add(createSidePanel(), BorderLayout.EAST);
		panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		this.add(panel, BorderLayout.CENTER);
		
		status = new StatusBar();
		JPanel statusBar = new JPanel(new BorderLayout());
		statusBar.setBorder(BorderFactory.createLoweredSoftBevelBorder());
		statusBar.add(status, BorderLayout.WEST);
		this.add(statusBar, BorderLayout.SOUTH);
	}
	
	public JPanel createMainPanel() {
		JPanel listPanel = new JPanel(new BorderLayout());
		
		photoModel = new DefaultListModel<Photo>();
		
		//Setting up the list and listmodel
		photos = new JList<Photo>(photoModel);
		photos.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		photos.setSelectedIndex(-1);
		photos.setLayoutOrientation(JList.VERTICAL);
		photos.setCellRenderer(new PhotoRenderer());
		photos.setVisibleRowCount(1);
				
		//Adding a scrollpane and doing some styling
		JScrollPane scrollUsers = new JScrollPane(photos);
		
		createAlbumBtn = new JButton("Create New Album");
		createAlbumBtn.addActionListener(this);
		JPanel buttonPanel = new JPanel();
		
		listPanel.add(scrollUsers, BorderLayout.CENTER);
		listPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10),
				BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Photos"),
						BorderFactory.createEmptyBorder(10, 10, 10, 10))));
		
		listPanel.add(buttonPanel, BorderLayout.SOUTH);
		return listPanel;
	}
	
	public JPanel createSidePanel() {
		JPanel searchPanel = createSearchPanel();
		
		backBtn = new JButton("Back to Albums");
		backBtn.addActionListener(this);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(backBtn);
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(searchPanel, BorderLayout.CENTER);
		panel.add(buttonPanel, BorderLayout.SOUTH);
		
		return panel;
	}
	
	public JPanel createChoicePanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		Insets radioMargins = new Insets(10,10,10,10);
		byTag = new JRadioButton("By Tag");
		byTag.setSelected(true);
		byTag.addActionListener(this);
		byTag.setMargin(radioMargins);
		
		byDate = new JRadioButton("By Date");
		byDate.addActionListener(this);
		byDate.setMargin(radioMargins);

		ButtonGroup choice = new ButtonGroup();
		choice.add(byDate);
		choice.add(byTag);
		
		panel.add(byDate, gbc);
		panel.add(byTag, gbc);
		
		return panel;
	}
	
 	public JPanel createSearchPanel() {
		//searchPane = new JTabbedPane();
		//searchPane.addTab("By Tag", null, createTagSearchPanel(), "Search By Tag");
		
		//searchPane.addTab("By Date Range", null, createDateSearchPanel(), "Search By Date Range");
		searchPanel = new JPanel(new CardLayout());
		searchPanel.add(createTagSearchPanel(), toTag);
		searchPanel.add(createDateSearchPanel(), toDate);
 		
		searchBtn = new JButton("Search");
		searchBtn.addActionListener(this);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(searchBtn);
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(createChoicePanel(), BorderLayout.NORTH);
		//panel.add(searchPane, BorderLayout.CENTER);
		panel.add(searchPanel, BorderLayout.CENTER);
		panel.add(buttonPanel, BorderLayout.SOUTH);
		
		return panel;
	}
	
	public JPanel createTagSearchPanel() {
		listModel = new DefaultListModel<Tag>();
		
		//Setting up the list and listmodel
		tags = new JList<Tag>(listModel);
		tags.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		tags.setSelectedIndex(-1);
		tags.setLayoutOrientation(JList.VERTICAL);
		tags.setVisibleRowCount(1);
		tags.setCellRenderer(new TagRenderer());
		tags.addListSelectionListener(this);
		
		//Adding a scrollpane and doing some styling
		JScrollPane scrollUsers = new JScrollPane(tags);

		removeTagBtn = new JButton("Remove Tag");
		removeTagBtn.addActionListener(this);
		
		JLabel typeLabel = new JLabel("Type: ");
		type = new JTextField(10);
		JPanel typePanel = new JPanel(new BorderLayout());
		typePanel.add(typeLabel, BorderLayout.WEST);
		typePanel.add(type, BorderLayout.EAST);
		
		JLabel valueLabel = new JLabel("Value: ");
		value = new JTextField(10);
		JPanel valuePanel = new JPanel(new BorderLayout());
		valuePanel.add(valueLabel, BorderLayout.WEST);
		valuePanel.add(value, BorderLayout.EAST);
		
		addTagBtn = new JButton("Add Tag");
		addTagBtn.addActionListener(this);
		JPanel addPanel = new JPanel();
		addPanel.add(addTagBtn);
		
		JPanel inputPanel = new JPanel(new GridLayout(0,1));
		inputPanel.add(typePanel);
		inputPanel.add(valuePanel);
		
		JPanel addTagPanel = new JPanel(new BorderLayout());
		addTagPanel.add(inputPanel, BorderLayout.CENTER);
		addTagPanel.add(addPanel, BorderLayout.SOUTH);
		addTagPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("New Tag"), 
				BorderFactory.createEmptyBorder(20, 20, 20, 20)));
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(removeTagBtn);
		
		JPanel listPanel = new JPanel(new GridLayout(1,1));
		listPanel.add(scrollUsers);
		listPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		
		JPanel tagPanel = new JPanel(new BorderLayout());
		tagPanel.add(addTagPanel, BorderLayout.NORTH);
		tagPanel.add(listPanel, BorderLayout.CENTER);
		tagPanel.add(buttonPanel, BorderLayout.SOUTH);
		tagPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Tags"), 
				BorderFactory.createEmptyBorder(20, 20, 20, 20)));
		
		return tagPanel;
	}
	
	public JPanel createDatePanel(JTextField year, JTextField month, JTextField day, JTextField hour, JTextField minute, JTextField second, String label) {
		JLabel yearLabel = new JLabel("Year: ");
		JPanel yearPanel = new JPanel(new BorderLayout());
		yearPanel.add(yearLabel, BorderLayout.WEST);
		yearPanel.add(year, BorderLayout.EAST);
		
		JLabel monthLabel = new JLabel("Month: ");
		JPanel monthPanel = new JPanel(new BorderLayout());
		monthPanel.add(monthLabel, BorderLayout.WEST);
		monthPanel.add(month, BorderLayout.EAST);

		JLabel dayLabel = new JLabel("Day: ");
		JPanel dayPanel = new JPanel(new BorderLayout());
		dayPanel.add(dayLabel, BorderLayout.WEST);
		dayPanel.add(day, BorderLayout.EAST);

		JLabel hourLabel = new JLabel("Hour: ");
		JPanel hourPanel = new JPanel(new BorderLayout());
		hourPanel.add(hourLabel, BorderLayout.WEST);
		hourPanel.add(hour, BorderLayout.EAST);

		JLabel minuteLabel = new JLabel("Minute: ");
		JPanel minutePanel = new JPanel(new BorderLayout());
		minutePanel.add(minuteLabel, BorderLayout.WEST);
		minutePanel.add(minute, BorderLayout.EAST);

		JLabel secondLabel = new JLabel("Second: ");
		JPanel secondPanel = new JPanel(new BorderLayout());
		secondPanel.add(secondLabel, BorderLayout.WEST);
		secondPanel.add(second, BorderLayout.EAST);
		
		JPanel datePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.weightx = 1;
        gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		datePanel.add(yearLabel, gbc);
		gbc.anchor = GridBagConstraints.EAST;
		datePanel.add(year, gbc);

		gbc.gridy++;
		gbc.anchor = GridBagConstraints.WEST;
		datePanel.add(monthLabel, gbc);
		gbc.anchor = GridBagConstraints.EAST;
		datePanel.add(month, gbc);

		gbc.gridy++;
		gbc.anchor = GridBagConstraints.WEST;
		datePanel.add(dayLabel, gbc);
		gbc.anchor = GridBagConstraints.EAST;
		datePanel.add(day, gbc);

		gbc.gridy++;
		gbc.anchor = GridBagConstraints.WEST;
		datePanel.add(hourLabel, gbc);
		gbc.anchor = GridBagConstraints.EAST;
		datePanel.add(hour, gbc);

		gbc.gridy++;
		gbc.anchor = GridBagConstraints.WEST;
		datePanel.add(minuteLabel, gbc);
		gbc.anchor = GridBagConstraints.EAST;
		datePanel.add(minute, gbc);

		gbc.gridy++;
		gbc.anchor = GridBagConstraints.WEST;
		datePanel.add(secondLabel, gbc);
		gbc.anchor = GridBagConstraints.EAST;
		datePanel.add(second, gbc);
		
		datePanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(label), 
				BorderFactory.createEmptyBorder(20, 20, 20, 20)));
		
		return datePanel;
	}
	
	public JPanel createDateSearchPanel() {
		JPanel datePanel = new JPanel();
		
		year1 = new JTextField(4);
		month1 = new JTextField(2);
		day1 = new JTextField(2);
		hour1 = new JTextField(2);
		minute1 = new JTextField(2);
		second1 = new JTextField(2);
		datePanel.add(createDatePanel(year1, month1, day1, hour1, minute1, second1, "Start date"));

		year2 = new JTextField(4);
		month2 = new JTextField(2);
		day2 = new JTextField(2);
		hour2 = new JTextField(2);
		minute2 = new JTextField(2);
		second2 = new JTextField(2);
		datePanel.add(createDatePanel(year2, month2, day2, hour2, minute2, second2, "End date"));
		
		datePanel.setVisible(false);
		datePanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Date"), 
				BorderFactory.createEmptyBorder(20, 20, 20, 20)));
		return datePanel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		
		if(source == backBtn) {
			parent.showAlbums();
		} else if(source == byDate) {
			((CardLayout)searchPanel.getLayout()).show(searchPanel, toDate);
		} else if(source == byTag) {
			((CardLayout)searchPanel.getLayout()).show(searchPanel, toTag);
		} else if(source == addTagBtn) {
			String typeString = type.getText();
			String valueString = value.getText();
			if(valueString.isEmpty()) {
				error("Tag must have a value");
			} else {
				Tag newTag = new Tag(typeString, valueString);
				if(listModel.contains(newTag)) {
					error("Tag is already in list.");
				} else {
					listModel.addElement(newTag);
				}
			}
		} else if(source == searchBtn) {
			if(byDate.isSelected()) {
				String firstYear = year1.getText();
				String firstMonth = month1.getText();
				String firstDay = day1.getText();
				String firstHour = hour1.getText();
				String firstMinute = minute1.getText();
				String firstSecond = second1.getText();
				
				String date1 = DateUtils.createDateString(firstYear, firstMonth, firstDay, firstHour, firstMinute, firstSecond);

				if(date1 == null ) {
					error("Invalid start date");
					return;
				}

				String secondYear = year2.getText();
				String secondMonth = month2.getText();
				String secondDay = day2.getText();
				String secondHour = hour2.getText();
				String secondMinute = minute2.getText();
				String secondSecond = second2.getText();
				
				String date2 = DateUtils.createDateString(secondYear, secondMonth, secondDay, secondHour, secondMinute, secondSecond);
				
				if(date2 == null) {
					error("Invalid end date");
					return;
				}
				List<Photo> photos = parent.searchByDate(date1, date2);
				if(photos == null) {
					error("Invalid date range");
					return;
				}
				if(photos.size() == 0) {
					message("No results found. Try a different search.");
				} else {
					photoModel.clear();
					for(Photo photo : photos) {
						photoModel.addElement(photo);
					}
					message("");
				}
				
			} else {
				List<Tag> tagList = Collections.list(listModel.elements());
				List<Photo> photos = parent.searchByTag(tagList);
				if(photos == null) {
					error("Invalid date range");
					return;
				}
				if(photos.size() == 0) {
					message("No results found. Try a different search.");
				} else {
					photoModel.clear();
					for(Photo photo : photos) {
						photoModel.addElement(photo);
					}
					message("");
				}
			}
		} else if(source == removeTagBtn) {
			if(tags.getSelectedIndex() != -1) {
				Tag tag = tags.getSelectedValue();
				listModel.removeElement(tag);
				String message = "No longer searching for photos with tag ";
				if(!tag.getType().equals("")) {
					message += "of type " + tag.getType();
				}
				message += "with value " + tag.getValue();
				message(message);
			} else {
				error("No tag selected for removal.");
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Utility for putting messages to the statusbar
	 * @param message
	 */
	public void error(String message) {
		status.setForeground(parent.red);
		status.setText(message);
	}
	
	/**
	 * Utility for putting errors to the statusbar
	 * @param message
	 */	
	public void message(String message) {
		status.setForeground(parent.black);
		status.setText(message);
	}
}
