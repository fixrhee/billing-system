		<aside class="main-sidebar sidebar-dark-primary elevation-4">
			<!-- Brand Logo -->
			<a href="/admin/index" class="brand-link"> <img
				src="dist/img/AdminLTELogo.png" alt="AdminLTE Logo"
				class="brand-image img-circle elevation-3" style="opacity: .8">
				<span class="brand-text font-weight-light">AdminVS</span>
			</a>

			<!-- Sidebar -->
			<div class="sidebar">
				<!-- Sidebar user panel (optional) -->
				<div class="user-panel mt-3 pb-3 mb-3 d-flex">
					<div class="image">
						<img src="dist/img/user2-160x160.jpg"
							class="img-circle elevation-2" alt="User Image">
					</div>
					<div class="info">
						<a href="#" class="d-block">${name}</a>
					</div>
				</div>

				<!-- Sidebar Menu -->
				<nav class="mt-2">
					<ul class="nav nav-pills nav-sidebar flex-column"
						data-widget="treeview" role="menu" data-accordion="false">
						<!-- Add icons to the links using the .nav-icon class
          			     with font-awesome or any other icon font library -->
						<li class="nav-item">
							<a href="index" class="nav-link <c:if test = "${menu eq 'dashboard'}">active</c:if>">
						<i class="nav-icon fas fa-tachometer-alt"></i>
							<p>Dashboard</p>
						</a></li>
						<li class="nav-item">
							<a href="outlet" class="nav-link <c:if test = "${menu eq 'outlet'}">active</c:if>">
						<i class="nav-icon fas fa-store"></i>
								<p>Outlet</p>
						</a></li>
						<li class="nav-item">
							<a href="product" class="nav-link <c:if test = "${menu eq 'product'}">active</c:if>">
						<i class="nav-icon fas fa-boxes"></i>
								<p>
									Product <span class="right badge badge-danger">New</span>
								</p>
						</a></li>
						<li class="nav-item has-treeview"><a href="#"
							class="nav-link"> <i class="nav-icon fas fa-ticket-alt"></i>
								<p>
									Voucher <i class="fas fa-angle-left right"></i>
								</p>
						</a>
							<ul class="nav nav-treeview">
								<li class="nav-item"><a href="voucher"
									class="nav-link <c:if test = "${menu eq 'voucher'}">active</c:if>"> <i class="far fa-circle nav-icon"></i>
										<p>Manage Voucher</p>
								</a></li>
								<li class="nav-item"><a href="published"
									class="nav-link <c:if test = "${menu eq 'published'}">active</c:if>"> <i class="far fa-circle nav-icon"></i>
										<p>Published Voucher</p>
								</a></li>
							</ul></li>

					</ul>
				</nav>
				<!-- /.sidebar-menu -->
			</div>
			<!-- /.sidebar -->
		</aside>