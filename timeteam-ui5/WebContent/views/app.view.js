sap.ui.jsview("views.app", {

	/**
	 * Specifies the Controller belonging to this View. In the case that it is
	 * not implemented, or that "null" is returned, this View does not have a
	 * Controller.
	 * 
	 * @memberOf views.main
	 */
	getControllerName : function() {
		return "views.app";
	},

	/**
	 * Is initially called once after the Controller has been instantiated. It
	 * is the place where the UI is constructed. Since the Controller is given
	 * to this method, its event handlers can be attached right away.
	 * 
	 * @memberOf views.main
	 */
	createContent : function(oController) {
		var oShell = new sap.ui.ux3.Shell({
			id : "mainShell",
			appTitle : "TimeTeam",
			appIcon : "images/sap-logo.png",
			appIconTooltip : "TimeTeam",
			showLogoutButton : false,
			showSearchTool : false,
			showInspectorTool : false,
			showFeederTool : false,

			/* Menu */
			worksetItems : [ new sap.ui.ux3.NavigationItem("wi_overview", {
				key : "wi_overview",
				text : "Overview"
			}), new sap.ui.ux3.NavigationItem("wi_tasks", {
				key : "wi_tasks",
				text : "Tasks"
			}) ],

			worksetItemSelected : function(oEvent) {
				var sId = oEvent.getParameter("id");
				var oShell = oEvent.oSource;
				switch (sId) {
				case "wi_overview":
					oShell.setContent(sap.ui.view({id:"viewOverview", viewName:"views.overview.overview", type:sap.ui.core.mvc.ViewType.JS}), true); 
					break;
				case "wi_tasks":
					oShell.setContent(sap.ui.view({id:"viewTaskList", viewName:"views.task.taskList", type:sap.ui.core.mvc.ViewType.JS}), true);
					break;
				default:
					break;
				}
			},
		});		
		
		oShell.addContent(sap.ui.view({id:"viewOverview", viewName:"views.overview.overview", type:sap.ui.core.mvc.ViewType.JS})); 
		oShell.placeAt("content");
	}

});
