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
		oModel.loadData("https://timeteami305363trial.hanatrial.ondemand.com/timeteam-1.0.0/services/rest/TaskService/list");
		sap.ui.getCore().setModel(oModel);
		
		var oTable = new sap.ui.table.Table({  
			   title : "All Tasks",  
			   visibleRowCount : 10,  
			   selectionMode : sap.ui.table.SelectionMode.Single,  
			   navigationMode : sap.ui.table.NavigationMode.Paginator,
			   // bind the core-model to this table by aggregating player-Array
			   rows: '{/tasks}'
			});
	

		oTable.addColumn(new sap.ui.table.Column({  
			   label : new sap.ui.commons.Label({  
			      text : "Name"  
			   }),  
			   template : new sap.ui.commons.TextView({
			      text: '{name}'
			   }),
			   width : "10px"  
			}));
		
		
		return oTable;
	}

});
