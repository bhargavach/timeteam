sap.ui.jsview("views.task.taskList", {

	/**
	 * Specifies the Controller belonging to this View. In the case that it is
	 * not implemented, or that "null" is returned, this View does not have a
	 * Controller.
	 * 
	 * @memberOf views.task.taskList
	 */
	getControllerName : function() {
		return "views.task.taskList";
	},

	/**
	 * Is initially called once after the Controller has been instantiated. It
	 * is the place where the UI is constructed. Since the Controller is given
	 * to this method, its event handlers can be attached right away.
	 * 
	 * @memberOf views.task.taskList
	 */
	createContent : function(oController) {

		var oModel = new sap.ui.model.json.JSONModel();
		var oOverlayContainer = new sap.ui.ux3.OverlayContainer();
		var oTable;

		oModel.loadData("https://timeteami305363trial.hanatrial.ondemand.com/timeteam-1.0.0/services/rest/TaskService/list");
		sap.ui.getCore().setModel(oModel);

		oTable = new sap.ui.table.Table({
			title : "All Tasks",
			visibleRowCount : 10,
			selectionMode : sap.ui.table.SelectionMode.Single,
			navigationMode : sap.ui.table.NavigationMode.Paginator,
			toolbar : new sap.ui.commons.Toolbar({
				items : [ new sap.ui.commons.Button({
					lite : true,
					icon : "images/plus.png",
					press : function(oEvent) {
						if (!oOverlayContainer.isOpen()) {
							oOverlayContainer.open();
						}
					}
				}) ]
			}),
			// bind the core-model to this table by aggregating player-Array
			rows : '{/tasks}'
		});

		oTable.addColumn(new sap.ui.table.Column({
			label : new sap.ui.commons.Label({
				text : "Name"
			}),
			template : new sap.ui.commons.TextView({
				text : '{name}'
			}),
			width : "10px"
		}));

		/* TASK ADD OVERLAY */
		var oPanel = new sap.ui.commons.Panel();
		oPanel.setTitle(new sap.ui.core.Title({
			text : "Add Task"
		}));
		var oMatrix = new sap.ui.commons.layout.MatrixLayout({
			layoutFixed : true,
			width : '30%',
			columns : 2
		});
		oMatrix.setWidths('40%', '60%');

		// Create a simple form within the layout
		var oLabel = new sap.ui.commons.Label({
			text : 'Name'
		});
		var oInput = new sap.ui.commons.TextField({
			value : 'Mustermann',
			width : '100%'
		});
		oLabel.setLabelFor(oInput);
		oMatrix.createRow(oLabel, oInput);

		oLabel = new sap.ui.commons.Label({
			text : 'First Name'
		});
		var oDropdownBox3 = new sap.ui.commons.DropdownBox("oDropdown");
		var oItemTemplate1 = new sap.ui.core.ListItem();
		oItemTemplate1.bindProperty("text", "name");
		oItemTemplate1.bindProperty("key", "id");
		oItemTemplate1.bindProperty("enabled", "enabled");
		oDropdownBox3.bindItems("/projects", oItemTemplate1);
		oLabel.setLabelFor(oInput);
		oMatrix.createRow(oLabel, oDropdownBox3);

		// Add the form to the panels content area
		oPanel.addContent(oMatrix);

		// Add some buttons to the panel header
		oPanel.addButton(new sap.ui.commons.Button({
			lite : true,
			icon : "images/check.png",
			press : function(oEvent) {
				saveTask();
				if (oOverlayContainer.isOpen()) {
					oOverlayContainer.close();
				}
				//closeTaskOverlayOpenHandler(oEvent);
			}
		}));
		oPanel.addButton(new sap.ui.commons.Button({
			lite : true,
			icon : "images/cancel.png",
			press : function(oEvent){
				if (oOverlayContainer.isOpen()) {
					oOverlayContainer.close();
				}
					//closeTaskOverlayOpenHandler(oEvent);
			}
		}));

		oOverlayContainer.addContent(oPanel);
		oOverlayContainer.attachClose(closeTaskOverlayOpenHandler);
		oOverlayContainer.attachOpen(openTaskOverlayOpenHandler);
		//oOverlayContainer.attachOpenNew(handler);

		function openTaskOverlayOpenHandler(oEvent) {
			var oModel = new sap.ui.model.json.JSONModel();
			oModel.loadData("https://timeteami305363trial.hanatrial.ondemand.com/timeteam-1.0.0/services/rest/ProjectService/list");
			sap.ui.getCore().setModel(oModel);
		}
		
		function closeTaskOverlayOpenHandler(oEvent) {
			var oModel = new sap.ui.model.json.JSONModel();
			oModel.loadData("https://timeteami305363trial.hanatrial.ondemand.com/timeteam-1.0.0/services/rest/TaskService/list");
			sap.ui.getCore().setModel(oModel);
		}
		
		function saveTask(){
			
		}

		return oTable;
	}

});
