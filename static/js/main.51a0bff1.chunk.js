(this["webpackJsonpcatch-pokemon"]=this["webpackJsonpcatch-pokemon"]||[]).push([[0],{43:function(e,t,n){e.exports=n(50)},48:function(e,t,n){},49:function(e,t,n){},50:function(e,t,n){"use strict";n.r(t);var a,r,c,i=n(1),o=n.n(i),u=n(37),l=n.n(u),m=(n(48),n(17)),p=(n(49),n(60)),s=n(38),d=n(35),b=n(58),f=n(54),j=n(61),O=n(55),k=n(59),E=n(39),g=n(53),h=n(57),v=n(40),y=n(56),P=function(e,t){var n=new f.a(0),a=new f.a(0),r=Object(j.a)(n.pipe(Object(s.a)((function(e){return{id:e,type:"add"}}))),a.pipe(Object(s.a)((function(e){return{id:e,type:"rem"}})))).pipe(Object(g.a)((function(t,n){return"add"===n.type?t.length===e?t:[].concat(Object(d.a)(t),[n.id]):t.filter((function(e){return e!==n.id}))}),[]));return{pokemonIdsBeingCaught:n,pokemonIdsBeingReleased:a,freePokemons:Object(O.a)([r,t]).pipe(Object(s.a)((function(e){var t=Object(m.a)(e,2),n=t[0];return t[1].filter((function(e){return!n.includes(e.id)}))}))),caughtPokemons:Object(O.a)([r,t]).pipe(Object(s.a)((function(e){var t=Object(m.a)(e,2),n=t[0];return t[1].filter((function(e){return n.includes(e.id)}))})))}}(6,(a=500,r=151,c=function(e){return Object(y.a)("https://pokeapi.co/api/v2/pokemon/".concat(e),{selector:function(e){return e.json()}})},Object(b.a)(a).pipe(Object(s.a)((function(e){return e+1})),Object(k.a)(r),Object(E.a)(c),Object(s.a)((function(e){return{id:e.id,name:e.name,sprite:e.sprites.front_default,type:e.types[0].type.name}})),Object(g.a)((function(e,t){return[].concat(Object(d.a)(e),[t])}),[]),Object(h.a)(),Object(v.a)()))),C=P.pokemonIdsBeingCaught,I=P.pokemonIdsBeingReleased,N=P.freePokemons,w=P.caughtPokemons;var B=function(){var e=Object(p.a)((function(e){return e.pipe(Object(s.a)((function(e){return parseInt(e.currentTarget.id)}))).subscribe(C),N}),[]),t=Object(m.a)(e,2),n=t[0],a=t[1],r=Object(p.a)((function(e){return e.pipe(Object(s.a)((function(e){return parseInt(e.currentTarget.id)}))).subscribe(I),w}),[]),c=Object(m.a)(r,2),i=c[0],u=c[1];return o.a.createElement("div",{className:"App"},o.a.createElement("div",{className:"CaughtPokemonsContainer"},u.map((function(e){return o.a.createElement("div",{className:"CaughtPokemon",id:e.id.toString(),key:e.name,onClick:i},o.a.createElement("table",null,o.a.createElement("tbody",null,o.a.createElement("tr",null,o.a.createElement("td",{rowSpan:3},o.a.createElement("img",{src:e.sprite})),o.a.createElement("td",null,"ID:"),o.a.createElement("td",null,e.id)),o.a.createElement("tr",null,o.a.createElement("td",null,"Name:"),o.a.createElement("td",null,e.name)),o.a.createElement("tr",null,o.a.createElement("td",null,"Type:"),o.a.createElement("td",null,e.type)))))}))),o.a.createElement("div",{className:"FreePokemonsContainer"},o.a.createElement("div",{className:"FreePokemons"},a.map((function(e){return o.a.createElement("div",{className:"FreePokemon",id:e.id.toString(),key:e.name,onClick:n},o.a.createElement("img",{src:e.sprite}))})))))};l.a.render(o.a.createElement(o.a.StrictMode,null,o.a.createElement(B,null)),document.getElementById("root"))}},[[43,1,2]]]);
//# sourceMappingURL=main.51a0bff1.chunk.js.map