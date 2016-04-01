var dataFromServer = [{
		"id" : "1",
		"url" : "http://tinyurl.com/1",
		"bicycle" : {
			"name" : "forward 1",
			"price" : "100"
		}
	}, {
		"id" : "2",
		"url" : "http://tinyurl.com/2",
		"bicycle" : {
			"name" : "forward 2",
			"price" : "100"
		}
	}, {
		"id" : "3",
		"url" : "http://tinyurl.com/3",
		"bicycle" : {
			"name" : "forward 3",
			"price" : "150"
		}
	}
];


var dataToServer = [];

var ProductList = React.createClass({
		displayName : "ProductList",
		handleClick : function (product) {
			console.log("handleClick called [" + product.id + "]");
			this.props.onProductTransfer(product);
		},
		render : function () {
			var this_ = this;
			//console.log("data["+ JSON.parse(data) +"]");
			var products = this.props.data.map(function (product, i) {
				console.log("product.id[" 
				+ product.id + "]");
					return  <Product callback = {this_.handleClick} key={product.id} product={product} className = 'product' />
				});
			return (
			<div className='products-list'>{products}</div> 
				 )
		}
	});

//product
var Product = React.createClass({
		displayName : "Product",
		toggleTransfer : function () {
			console.log("clicked...");
			this.setState({
				added : !this.state.added
			});
			console.log("productToTransfer id[" + this.props.product.id + "]");
			this.props.callback(this.props.product);
		},

		getInitialState : function () {
			return {
				added : false
			}
		},

		render : function () {
			var buttonClass = this.state.added ? 'active' : '';

			return (
				 <div className = 'product'>< span > {this.props.product.url} </span> <br/>
				 <span> {this.props.product.bicycle.name}<  / span >  < br /  >
				 <span> {this.props.product.bicycle.price}
				 </span>  <br/>
				 <button onClick = {this.toggleTransfer} className = {buttonClass}> Move </button ></div> )
		}
	});

var ProductToSend = React.createClass({
		handleClick : function (product) {
			console.log("handleClick called [" + product.id + "]");
			this.props.onProductTransfer(product);
		},

		render : function () {
			var this_ = this;
			//console.log("data["+ JSON.parse(data) +"]");
			var products = this.props.data.map(function (product, i) {
					console.log("product " + i + " url[" + product.url + "]");
					return  < Product callback = {this_.handleClick} key = {product.id} product = {product}	className = 'product' /  >
				});

			return (
				 < div className = 'products-list' > {products}<  / div > )
		}
	});

var ProductsWrapper = React.createClass({

		getInitialState : function () {
			return {
				dataFrom : [],
				dataTo : []
			};
		},
		componentDidMount : function () {
			this.setState({
				//add mock data todo: from server
				dataFrom : dataFromServer,
				dataTo : dataToServer
			});
		},
		handleProductTransfer : function(product){
			dataToServer.push(product);
			for(var i=0; i<dataFromServer.length; i++){
				var p = dataFromServer[i];
				console.log("p.id[" + p.id+"] product.id[" + product.id + "]");
				if(p.id === product.id){
					console.log("removing " + i + " array element");
					dataFromServer.splice(i,1);
				}
			}
					
			this.setState({dataTo: dataToServer});
			this.setState({dataFrom: dataFromServer});
		},
		handleProductTransferBack : function(product){
			dataFromServer.push(product);
			for(var i=0; i<dataToServer.length; i++){
				var p = dataToServer[i];
				console.log("p.id[" + p.id+"] product.id[" + product.id + "]");
				if(p.id === product.id){
					console.log("removing " + i + " array element");
					dataToServer.splice(i,1);
				}
			}
					
			this.setState({dataTo: dataToServer});
			this.setState({dataFrom: dataFromServer});
		},

		render : function () {
			return (
				 <div>
				 < div id = "list" >  <ProductList data={this.state.dataFrom} onProductTransfer={this.handleProductTransfer}/></div >
				 < div id = "result-list" >  <ProductToSend  data={this.state.dataTo} onProductTransfer={this.handleProductTransferBack}/>  </div >
				 </div> )
		}

	});

ReactDOM.render( < ProductsWrapper/> , document.getElementById("wrapper"));
