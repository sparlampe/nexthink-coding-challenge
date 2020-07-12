import React from 'react';
import './App.css';
import {useEventCallback} from "rxjs-hooks";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {createCatchPokemonStreams, pokemonFetcher, PokemonInfo, pokemonObservable} from "./pokemonStreams";

const allPokemons = pokemonObservable(500,151, pokemonFetcher)
const {pokemonIdsBeingCaught, pokemonIdsBeingReleased, freePokemons, caughtPokemons} = createCatchPokemonStreams(6,allPokemons)

function App() {
    const [catchCallback, freePokemon] = useEventCallback((event$:Observable<React.MouseEvent<HTMLElement>>) => {
            event$.pipe(map(e => parseInt(e.currentTarget.id))).subscribe(pokemonIdsBeingCaught)
            return freePokemons
        }, [] as PokemonInfo[])
    
    const [releaseCallback, caughtPokemon] = useEventCallback((event$:Observable<React.MouseEvent<HTMLElement>>) => {
            event$.pipe(map(e => parseInt(e.currentTarget.id))).subscribe(pokemonIdsBeingReleased)
            return caughtPokemons
        }, [] as PokemonInfo[])

    return (
        <div className="App">
            <div className="CaughtPokemonsContainer">
                {caughtPokemon.map(pokemon => (
                    <PokemonInfoPane pokemon={pokemon} onClick={releaseCallback}/>
                ))}
            </div>
            <div className="FreePokemonsContainer">
                <div className="FreePokemons">
                {freePokemon.map(pokemon => (
                    <div className="FreePokemon" id={pokemon.id.toString()} key={pokemon.name} onClick={catchCallback}>
                        <img src={pokemon.sprite}></img>
                    </div>
                    
                ))}
                </div>
            </div>
        </div>
    );
}

export default App;


const PokemonInfoPane = ({pokemon, onClick}: {pokemon: PokemonInfo, onClick: (e: React.MouseEvent<HTMLElement, MouseEvent>) => void}) => 
        <div className="CaughtPokemon" id={pokemon.id.toString()} key={pokemon.name} onClick={onClick}>
            <table><tbody>
            <tr>
                <td rowSpan={3}><img src={pokemon.sprite}></img></td>
                <td>ID:</td>
                <td>{pokemon.id}</td>
            </tr>
            <tr>
                <td>Name:</td>
                <td>{pokemon.name}</td>
            </tr>
            <tr>
                <td>Type:</td>
                <td>{pokemon.type}</td>
            </tr>
            </tbody></table>
        </div>
