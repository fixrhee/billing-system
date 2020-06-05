		<!-- Header -->
				<%@include file="header.jsp" %>
		<!-- /.Header -->

		<!-- Main Sidebar Container -->
			<%@include file="sidebar.jsp" %>
		<!-- /.sidebar -->

		 <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1>Create New Invoice</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="index">Home</a></li>
              <li class="breadcrumb-item"><a href="invoice">Invoice</a></li>
              <li class="breadcrumb-item active">New Invoice</li>
            </ol>
          </div>
        </div>
      </div><!-- /.container-fluid -->
    </section>

    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-12">
          <div class="card">
            <!-- /.card-header -->
            <div class="card-body">
            
   	<!-- SmartWizard html -->
    	<div id="smartwizard">
        <ul class="nav">
            <li class="nav-item">
              <a class="nav-link" href="#step-1">
                <strong>Step 1</strong> <br>Select Billing
              </a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#step-2">
                <strong>Step 2</strong> <br>Invoice Item
              </a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#step-3">
                <strong>Step 3</strong> <br>Select Member
              </a>
            </li>
        </ul>

        <form action="submitInvoice" method="POST">
        <div class="tab-content">
            <div id="step-1" class="tab-pane" role="tabpanel" aria-labelledby="step-1">
               <div>&nbsp;</div>
                <div class="col-md-6">
	              <div>Select Billing to create the invoice from</div>
	              <div>&nbsp;</div>
                <div class="form-group">
                  <select id="billing" name="billing" class="form-control select2" style="width: 100%;">
                    <option selected="selected" value="billing0">Billing</option>
                    <option value="billing1">Billing 1</option>
                    <option value="billing2">Billing 2</option>
                    <option value="billing3">Billing 3</option>
                    <option value="billing4">Billing 4</option>
                    <option value="billing5">Billing 5</option>
                    <option value="billing6">Billing 6</option>
                  </select>
                  </div>
                </div>
                <div>&nbsp;</div>
                <hr />
            </div>
            <div id="step-2" class="tab-pane" role="tabpanel" aria-labelledby="step-2">
            		<div>&nbsp;</div>
                <div>Insert item components with the expected amount. Click <font color="blue"><b>Add More</b></font> to add more items.</div>
                <div>&nbsp;</div>
            <div class="form-group" id="dynamic_form">
                <div class="row">
	                <div class="col-md-6">
	                    <input type="text" name="item" id="item" placeholder="Enter Item Name" class="form-control">
	                </div>
	                <div class="col-md-3">
	                    <input type="number" step="1" min="0" class="form-control" name="amount" id="amount" placeholder="Enter Amount">
	                </div>
	                <div class="button-group">
	                    <a href="javascript:void(0)" class="btn btn-primary" id="plus5">Add More</a>
	                    <a href="javascript:void(0)" class="btn btn-danger" id="minus5">Remove</a>
	                </div>
	            </div>
          	  </div>
            <div>&nbsp;</div>
	    		<hr />
            <div>&nbsp;</div>
            <div>&nbsp;</div>
            <div>&nbsp;</div>
 		    <div>&nbsp;</div>
 		    <div>&nbsp;</div>
			<div>&nbsp;</div>
		    </div>
            <div id="step-3" class="tab-pane" role="tabpanel" aria-labelledby="step-3">
           	<div>&nbsp;</div>
          		<div>Select members to be billed by moving the list on the left box to the right box, and click <font color="blue"><b>Finish</b></font> to proceed.</div>
          	<div>&nbsp;</div>
               <div class="col-12">
                <div class="form-group">
                  <select id="member" name="member" class="duallistbox" multiple="multiple">
                    <option value="1" selected>Ari</option>
                    <option value="2">Achrial</option>
                    <option value="3">Fikri</option>
                    <option value="4">Ridho</option>
                    <option value="5">Beni</option>
                    <option value="6">Andre</option>
                    <option value="7">Rafli</option>
                  </select>
                </div>
                <!-- /.form-group -->
              </div>
              <!-- /.col -->
        			<div class="form-group">
                 <button type="submit" class="btn btn-info">Finish</button>
            		 </form>
            		 <hr />
              </div>   
         </div>
        </div>
    </div>
            </div>
            <!-- /.card-body -->
          </div>
          <!-- /.card -->
        </div>
        <!-- /.col -->
      </div>
      <!-- /.row -->                    
    </section>
    <!-- /.content -->
  </div>
  <!-- /.content-wrapper -->
		<!-- footer -->
			<%@include file="footer.jsp" %>
		<!-- /.footer -->

<script>
        $(document).ready(function() {
        	var dynamic_form =  $("#dynamic_form").dynamicForm("#dynamic_form","#plus5", "#minus5", {
		        limit:4,
		        formPrefix : "dynamic_form",
		        normalizeFullForm : false
		    });

       
		    $("#dynamic_form #minus5").on('click', function(){
		    	var initDynamicId = $(this).closest('#dynamic_form').parent().find("[id^='dynamic_form']").length;
		    	if (initDynamicId === 2) {
		    		$(this).closest('#dynamic_form').next().find('#minus5').hide();
		    	}
		    	$(this).closest('#dynamic_form').remove();
		    });

		
        });
</script>
    		
<script type="text/javascript">
        $(document).ready(function(){

            // Toolbar extra buttons
            var btnFinish = $('<button></button>').text('Finish')
                                             .addClass('btn btn-info')
                                             .on('click', function(){ alert('Finish Clicked'); });
            var btnCancel = $('<button></button>').text('Cancel')
                                             .addClass('btn btn-danger')
                                             .on('click', function(){ $('#smartwizard').smartWizard("reset"); });

            // Step show event
            $("#smartwizard").on("showStep", function(e, anchorObject, stepNumber, stepDirection, stepPosition) {
         	    $("#prev-btn").removeClass('disabled');
                $("#next-btn").removeClass('disabled');
                if(stepPosition === 'first') {
                    $("#prev-btn").addClass('disabled');
                    $('.sw-btn-group-extra').hide();
                } else if(stepPosition === 'last') {
                    $("#next-btn").addClass('disabled');
                } else {
                    $("#prev-btn").removeClass('disabled');
                    $("#next-btn").removeClass('disabled');
                }
            });

            // Smart Wizard
            $('#smartwizard').smartWizard({
                selected: 0,
                theme: 'dots',
                transition: {
                    animation: 'slide-horizontal', // Effect on navigation, none/fade/slide-horizontal/slide-vertical/slide-swing
                },
                toolbarSettings: {
                    toolbarPosition: 'bottom', // both bottom
                    //toolbarExtraButtons: [btnFinish, btnCancel]
                }
            });

            // External Button Events
            $("#reset-btn").on("click", function() {
                // Reset wizard
                $('#smartwizard').smartWizard("reset");
                return true;
            });

            $("#prev-btn").on("click", function() {
                // Navigate previous
                $('#smartwizard').smartWizard("prev");
                return true;
            });

            $("#next-btn").on("click", function() {
                // Navigate next
                $('#smartwizard').smartWizard("next");
                return true;
            });


            // Demo Button Events
            $("#got_to_step").on("change", function() {
                // Go to step
                var step_index = $(this).val() - 1;
                $('#smartwizard').smartWizard("goToStep", step_index);
                return true;
            });

            $("#is_justified").on("click", function() {
                // Change Justify
                var options = {
                  justified: $(this).prop("checked")
                };

                $('#smartwizard').smartWizard("setOptions", options);
                return true;
            });

            $("#animation").on("change", function() {
                // Change theme
                var options = {
                  transition: {
                      animation: $(this).val()
                  },
                };
                $('#smartwizard').smartWizard("setOptions", options);
                return true;
            });

            $("#theme_selector").on("change", function() {
                // Change theme
                var options = {
                  theme: $(this).val()
                };
                $('#smartwizard').smartWizard("setOptions", options);
                return true;
            });

        });
</script>

<script>
  $(function () {
    //Bootstrap Duallistbox
    $('.duallistbox').bootstrapDualListbox({
    selectorMinimalHeight: 250
    });
  })
</script>

<script type="text/javascript">
function findTotal(){
    var arr = document.getElementsByName('amount');
    var tot=0;
    for(var i=0;i<arr.length;i++){
        if(parseFloat(arr[i].value))
            tot += parseFloat(arr[i].value);
    }
    document.getElementById('total').value = tot;
    tot.toFixed(2);
}

</script>
    
<c:if test="${not empty fn:trim(notification)}">	
	<script type="text/javascript">
	$(function(){
 	 new PNotify({
        title: '${title}',
        text: '${message}',
        type: '${notification}',
        styling: 'bootstrap3'
        });
	});
	</script>
</c:if>
</body>
</html>
