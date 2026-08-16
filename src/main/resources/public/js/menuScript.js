const { useRef, useState, useEffect, createRef } = React;
const menuColor = "rgb(29, 109, 116)";
const calcColor = "rgb(29, 109, 116)";
const settingsColor = "rgb(29, 109, 116)";
/*--------------------
Items
--------------------*/
const items = [
{
  name: "home",
  color: menuColor,
  href: "#"
},
{
  name: "voedingstrafo",
  color: calcColor,
  href: "#" 
},
{
  name: "uitgangstrafo",
  color: calcColor,
  href: "#" 
},
{
  name: "smoorspoel",
  color: calcColor,
  href: "#" 
},
{
  name: "weetjes",
  color: menuColor,
  href: "#" 
},
{
  name: "diversen",
  color: menuColor,
  href: "#" 
},
{
  name: "winkelwagen",
  color: menuColor,
  href: "#" 
},
{
  name: "zoeken",
  color: menuColor,
  href: "#" 
},
{
  name: "instellingen",
  color: settingsColor,
  href: "#" 
},
{
  name: "cookies",
  color: settingsColor,
  href: "#" 
},
];

/*--------------------
Menu
--------------------*/
const Menu = ({ items }) => {
  const $root = useRef();
  const $indicator1 = useRef();
  const $indicator2 = useRef();
  const $items = useRef(items.map(createRef));
  const [active, setActive] = useState(0);

  const animate = () => {
    const menuOffset = $root.current.getBoundingClientRect();
    const activeItem = $items.current[active].current;
    const { width, height, top, left } = activeItem.getBoundingClientRect();

    const settings = {
      x: left - menuOffset.x,
      y: top - menuOffset.y,
      width: width,
      height: height,
      backgroundColor: items[active].color,
      ease: 'elastic.out(.7, .7)',
      duration: .8 };


    gsap.to($indicator1.current, {
      ...settings });


    gsap.to($indicator2.current, {
      ...settings,
      duration: 1 });

  };

  useEffect(() => {
    animate();
    window.addEventListener('resize', animate);

    return () => {
      window.removeEventListener('resize', animate);
    };
  
}, [active]);

  return /*#__PURE__*/(
    React.createElement("div", {
      ref: $root,
      className: "menu" 
},

    items.map((item, menuIndex) => /*#__PURE__*/
    React.createElement("a", {
      key: item.name,
      ref: $items.current[menuIndex],
      className: `item ${active === menuIndex ? 'active' : ''}`,
      onMouseEnter: () => {setActive(menuIndex);},
      onClick: () => {
			var query = "?ipAddress=" + pubIp;
			htmx.ajax("GET", "/" + item.name + query, "#contentDiv");
            htmx.ajax("GET", "/clear", "#vertMenuDiv");
            htmx.ajax("GET", "/" + item.name + "Hist" + query, "#infoDiv");
			objectBin = 1;
			savedValues = "";
		 },
      href: item.href 
},

    item.name)), /*#__PURE__*/


    React.createElement("div", {
      ref: $indicator1,
      className: "indicator" }), /*#__PURE__*/

    React.createElement("div", {
      ref: $indicator2,
      className: "indicator" })));



};


/*--------------------
App
--------------------*/
const App = () => {
  return /*#__PURE__*/(
    React.createElement("div", { className: "App" 
}, /*#__PURE__*/
    React.createElement(Menu, { items: items })));


};


/*--------------------
Render
--------------------*/
ReactDOM.render( /*#__PURE__*/React.createElement(App, null),
document.getElementById("menu"));