sap.ui.controller("views.overview.overview", {

/**
* Called when a controller is instantiated and its View controls (if available) are already created.
* Can be used to modify the View before it is displayed, to bind event handlers and do other one-time initialization.
* @memberOf views.overview.overview
*/
//	onInit: function() {
//
//	},

/**
* Similar to onAfterRendering, but this hook is invoked before the controller's View is re-rendered
* (NOT before the first rendering! onInit() is used for that one!).
* @memberOf views.overview.overview
*/
//	onBeforeRendering: function() {
//
//	},

/**
* Called when the View has been rendered (so its HTML is part of the document). Post-rendering manipulations of the HTML could be done here.
* This hook is the same one that SAPUI5 controls get after being rendered.
* @memberOf views.overview.overview
*/
//	onAfterRendering: function() {
//
//	},

/**
* Called when the Controller is destroyed. Use this one to free resources and finalize activities.
* @memberOf views.overview.overview
*/
//	onExit: function() {
//
//	}
	
	initModel: function(){
		var oModel = new sap.ui.model.json.JSONModel();
		oModel.loadData(" https://timeteami305363trial.hanatrial.ondemand.com/timeteam-1.0.0/services/rest/StatsService/timeAllocationByProject");
		sap.ui.getCore().setModel(oModel);

		var dataset = new sap.viz.ui5.data.FlattenedDataset({

			dimensions : [ {
				axis : 1,
				name : 'Project',
				value : "{name}"
			} ],

			measures : [ {
				name : 'Total Time',
				value : "{value}"
			} ],

			data : {
				path : "/datasets",
				factory : function() {
				}
			}
		});
		
		return dataset;
	}

});