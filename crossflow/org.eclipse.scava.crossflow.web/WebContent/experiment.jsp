<jsp:include page="header.jsp" />

<main role="main" id="app">

<section class="jumbotron text-center">
	<div class="container">
		<h1 class="jumbotron-heading">{{ experiment.title }}</h1>
		<p class="lead text-muted">{{ experiment.summary }}</p>
		
		<div class="btn-group" v-if="experiment.status == 'stopped'">
		  <button type="button" class="btn btn-success" v-on:click="startExperiment">Start</button>
		  <button type="button" class="btn btn-success dropdown-toggle dropdown-toggle-split" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
		    <span class="sr-only">Toggle Dropdown</span>
		  </button>
		  <div class="dropdown-menu">
		    <a class="dropdown-item" href="#" v-on:click="startExperiment">Master and worker</a>
		    <a class="dropdown-item" href="#" v-on:click="startExperimentMasterOnly">Master only</a>
		  </div>
		</div>

		<button v-on:click="stopExperiment"
			v-if="experiment.status == 'running'" class="btn btn-danger my-2">Stop</button>
		<button v-on:click="resetExperiment"
			v-if="experiment.status != 'running' && (experiment.cached || experiment.executed)" class="btn btn-information my-2">Reset</button>
	</div>
</section>

<section>
	<div class="container">
		<div class="row">
            <div class="col-sm-12 col-md-12 py-12">	
				<ul class="nav nav-tabs" id="myTab" role="tablist">
				  <li class="nav-item" v-for="(fileDescriptor,index) in experiment.fileDescriptors">
				    <a class="nav-link" :id="'tab' + index" data-toggle="tab" :href="'#home' + index" role="tab" :aria-controls="'home'+index" aria-selected="true">{{ fileDescriptor.title }}</a>
				  </li>
				  <li class="nav-item">
				    <a class="nav-link active" id="diagnostics-tab" data-toggle="tab" href="#diagnostics" role="tab" aria-controls="diagnostics" aria-selected="false">Diagnostics</a>
				  </li>
				</ul>
				<div class="tab-content" id="myTabContent">
				  <div class="tab-pane fade show" v-for="(fileDescriptor,index) in experiment.fileDescriptors" :id="'home'+index" role="tabpanel" :aria-labelledby="'home'+index+'-tab'">
				  	<p/>
				  	<table class="table table-striped" v-if="fileDescriptor.table">
				  		<thead v-if="fileDescriptor.table.header">
				  			<tr>
				  				<th v-for="cell in fileDescriptor.table.header.cells"> {{ cell }}</th>
				  			</tr>
				  		</thead>
				  		<tr v-for="row in fileDescriptor.table.rows">
				  			<td v-for="cell in row.cells"> {{ cell }}</td>
				  		</tr>
				  	</table>
				  </div>
				  <div class="tab-pane fade show active" id="diagnostics" role="tabpanel" aria-labelledby="diagnostics-tab">
				  <p>
					  <table class="table table-striped">
					  	<tr>
					  		<td>Broker</td>
					  		<td>
					  			<span class="badge badge-success" v-if="diagnostics.brokerRunning">running</span>
                    			<span class="badge badge-danger" v-if="!diagnostics.brokerRunning">stopped</span>
					  		</td>
					  	</tr>
					  	<tr>
					  		<td>Executed</td>
					  		<td>
					  			<span class="badge badge-success" v-if="experiment.executed">yes</span>
                    			<span class="badge badge-dark" v-if="!experiment.executed">no</span>
					  		</td>
					  	</tr>
					  	<tr>
					  		<td>Cached</td>
					  		<td>
					  			<span class="badge badge-success" v-if="experiment.cached">yes</span>
                    			<span class="badge badge-dark" v-if="!experiment.cached">no</span>
					  		</td>
					  	</tr>
					  	<tr><td>Serving from</td><td><code>{{ diagnostics.rootDirectory }}</code></td></tr>
					  </table>
				  </p>
				  </div>
				</div>
			</div>
		</div>
	</div>
</section>

</main>

<script>
	var app = new Vue({
		el : '#app',
		data : {
			message : 'Hello Vue!',
			experimentId : null,
			experiment : new Experiment(),
			diagnostics : new Diagnostics()
		},
		methods : {
			startExperimentMasterOnly : function(event) {
				crossflow.startExperiment(this.experimentId, false);
			},
			startExperiment : function(event) {
				crossflow.startExperiment(this.experimentId, true);
			},
			stopExperiment : function(event) {
				crossflow.stopExperiment(this.experimentId);
			},
			resetExperiment : function(event) {
				crossflow.resetExperiment(this.experimentId);
			}
		}
	})

	var transport = new Thrift.TXHRTransport("/org.eclipse.scava.crossflow.web/crossflow");
	var protocol = new Thrift.TJSONProtocol(transport);
	var crossflow = new CrossflowClient(protocol);

	var url = new URL(window.location.href);
	app.experimentId = url.searchParams.get("id");
	
	setInterval(function() {
		app.experiment = crossflow.getExperiment(app.experimentId);
		app.experiment.fileDescriptors.forEach(function(fileDescriptor){
			fileDescriptor.table = crossflow.getContent(fileDescriptor);
		});
	}, 1000);
	
	setInterval(function() {
		app.diagnostics = crossflow.getDiagnostics();
	}, 1000);
	
</script>

<jsp:include page="footer.jsp" />
