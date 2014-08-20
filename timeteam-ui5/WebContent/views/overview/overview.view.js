sap.ui.jsview("views.overview.overview", {

	/**
	 * Specifies the Controller belonging to this View. In the case that it is
	 * not implemented, or that "null" is returned, this View does not have a
	 * Controller.
	 * 
	 * @memberOf views.overview.overview
	 */
	getControllerName : function() {
		return "views.overview.overview";
	},

	/**
	 * Is initially called once after the Controller has been instantiated. It
	 * is the place where the UI is constructed. Since the Controller is given
	 * to this method, its event handlers can be attached right away.
	 * 
	 * @memberOf views.overview.overview
	 */
	createContent : function(oController) {
		
		var pie = new sap.viz.ui5.Pie({
			id : "pie",
			width : "80%",
			height : "400px",
			plotArea : {
			// 'colorPalette' : d3.scale.category20().range()
			},
			toolTip : {
				preRender : function(tooltipDomNode) {
					// Called before render tooltip.
					tooltipDomNode.append('div').text('Append more information in default tooltip.').style({
						'font-weight' : 'bold'
					});
				},
				postRender : function(tooltipDomNode) {
					// Called after tooltip is renderred.
					// tooltipDomNode.selectAll('.v-body-measure-value').style({'color':
					// 'red'});
					tooltipDomNode.selectAll('.v-body-measure-value').attr('style', 'color: red;');
				}
			},
			
			 title : { visible : true, text : 'Project time' },
			 
			dataset : oController.initModel(),

			layoutData : new sap.ui.layout.GridData({
				span : "L4 M6 S12"
			})
		});
		
		var oGrid = new sap.ui.layout.Grid({
			hSpacing : 0,
			vSpacing : 0,
			content : [ pie ]
		});

		return oGrid;

	}

});
