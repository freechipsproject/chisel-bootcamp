
// load accordian stuff
var styleAcc = document.createElement('style');
styleAcc.innerHTML = `
#container {
  margin: 0 auto;
	width: 100%;
}
#accordion input {
  display: none;
}
#accordion label {
	background: #eee;
	border-radius: .25em;
	cursor: pointer;
	display: block;
	margin-bottom: .125em;
	padding: .25em 1em;
	z-index: 20;
}
#accordion label:hover {
  background: #ccc;
}

#accordion input:checked + label {
	background: #ccc;
	border-bottom-right-radius: 0;
	border-bottom-left-radius: 0;
	color: white;
	margin-bottom: 0;
}
#accordion article {
	background: #f7f7f7;
	height:0px;
	overflow:hidden;
	z-index:10;
}
#accordion article p {
  padding: 1em;
}
#accordion input:checked article {
}
#accordion input:checked ~ article {
	border-bottom-left-radius: .25em;
	border-bottom-right-radius: .25em;
	height: auto;
	margin-bottom: .125em;
}
`;
document.head.appendChild(styleAcc);
