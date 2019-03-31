/* (!) filePath must specify a location accessible by the web server */
function loadFile(filePath) {
	  var result = null;
	  var xmlhttp = new XMLHttpRequest();
	  xmlhttp.open("GET", filePath, false);
	  xmlhttp.send();
	  if (xmlhttp.status==200) {
	    result = xmlhttp.responseText;
	  }
	  return result;
}

function main(container, experimentId) {
	
	let ws = null;
	const wsUri = 'ws://localhost:61614';
	const protocol = 'stomp';
	const mdBroadcaster = '/topic/StreamMetadataBroadcaster';
	
	try {
		ws = new WebSocket(wsUri, protocol);
	} catch(err) {
		console.warn('failed to connect to ' + wsUri + ' over ' + protocol + ' protocol.');
	}
	
	if ( ws == null ) {
		console.warn('failed to connect to ' + wsUri + ' over ' + protocol + ' protocol.');
		console.warn('make sure ActiveMQ broker is running')
		return; // do not proceed 
	}
	
	ws.onerror = (evt) => {
	      if (ws.readyState == 1) {
	        console.warn('ws normal error: ' + evt.type);
	      } else {
	    	  console.warn('ws error');
	      }
	};
	
	ws.onopen = () => {
	  ws.send('CONNECT\n\n\0');
	  console.log('connected to ' + wsUri + ' over ' + protocol + ' protocol');
	 
	  ws.send('SUBSCRIBE\ndestination:' + mdBroadcaster + '.' + experimentId + '\n\nack:auto\n\n\0');
	  console.log('subscribed to ' + mdBroadcaster);
	 
	  //ws.send('DISCONNECT\n\n\0');
	  //console.log('disconnect');
	};
	 
	ws.onmessage = (e) => {
	 console.log('onmessage: ' + e);
	  if (e.data.startsWith('MESSAGE')) {
		  console.log(e.data);
		  window.runtimeModelGraph.getTooltipForCell = function(cell) {
				return "<table border=1><tr><td>Size: n/a</td><td>InFlight: n/a</td><td>Subscribers: n/a</td></tr></table>";
			}
	  }
	 
	};

	/*
	 * TODO Animation: examples/animation.html / thread.html Markers:
	 * examples/control.html Orthogonal: examples/orthogonal.html Swimlanes:
	 * monitor.html
	 */
	var codec = new mxCodec();

	mxEvent.disableContextMenu(container);
	window.runtimeModelContainer = container;

	window.runtimeModelGraph = new mxGraph(window.runtimeModelContainer);
	window.runtimeModelGraph.getStylesheet().getDefaultEdgeStyle()['edgeStyle'] = 'orthogonalEdgeStyle';
	window.runtimeModelGraph.setTooltips(true);
	window.runtimeModelGraph.getTooltipForCell = function(cell) {
		return "<table border=1><tr><td>Size: n/a</td><td>InFlight: n/a</td><td>Subscribers: n/a</td></tr></table>";
	}

	window.runtimeModelParent = window.runtimeModelGraph.getDefaultParent();
	window.runtimeModelGraph.enabled = false;

	window.runtimeModelGraph.getModel().beginUpdate();
	try {

		loadStencils();
		
		$.getScript('experiments/' + experimentId + '/graph.abstract', function()
		{
			var model = window.runtimeModelGraph.getModel();
			
			var layout = new mxCompactTreeLayout(window.runtimeModelGraph, true);
			layout.execute(window.runtimeModelParent, model.cells[2]);
			
			var nodeEnc = codec.encode(model);
			var xml = mxUtils.getXml(nodeEnc);
		});
	
	} finally {
		// Updates the display
		window.runtimeModelGraph.getModel().endUpdate();
	}

};

