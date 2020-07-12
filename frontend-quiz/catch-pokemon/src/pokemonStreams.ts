import {BehaviorSubject, combineLatest, interval, merge, Observable} from "rxjs";
import {map, mergeMap, scan, take, publishReplay, refCount} from "rxjs/operators";

import {IPokemon} from "pokeapi-typescript";
import {fromFetch} from "rxjs/fetch";

export interface PokemonInfo {
    id: number,
    name: string,
    sprite: string,
    type: string
}

export const pokemonObservable = (delayMs: number, 
                                  numberToTake: number, 
                                  pokemonFetcher: (id: number) => Observable<IPokemon>
): Observable<PokemonInfo[]> =>  
    interval(delayMs)
    .pipe(
        map(v => v+1),
        take(numberToTake),
        mergeMap(pokemonFetcher),
        map<IPokemon, PokemonInfo>(p => ({
            id: p.id,
            name: p.name,
            sprite: p.sprites.front_default,
            type: p.types[0].type.name
        })),
        scan<PokemonInfo, PokemonInfo[]>((acc, one) => ([...acc, one]), []),
        publishReplay(),
        refCount()
    )

interface PokemonAction {id: number;}
type RemovePokemonAction = PokemonAction & {type: "rem"}
type AddPokemonAction = PokemonAction & {type: "add"}

export const createCatchPokemonStreams = (maxNumOfCaughtPokemons: number, pokemonObservable: Observable<PokemonInfo[]>) => {
    const pokemonIdsBeingCaught = new BehaviorSubject(0);
    const pokemonIdsBeingReleased = new BehaviorSubject(0);
    const caughtPokemonIds = merge(
        pokemonIdsBeingCaught.pipe(map<number,AddPokemonAction>(id => ({id, type: "add"}))),
        pokemonIdsBeingReleased.pipe(map<number,RemovePokemonAction>(id => ({id, type: "rem"})))
    ).pipe(
            scan((acc, value) => {
                    if(value.type==="add"){
                        return acc.length === maxNumOfCaughtPokemons? acc: [...acc, value.id]
                    } else {
                        return acc.filter(i=> i!==value.id)
                    }
                }
                , [] as Array<number>)
        )
    
    
    const freePokemons = combineLatest([caughtPokemonIds, pokemonObservable])
        .pipe(
            map(([ids, pokemons]) => pokemons.filter(p => !ids.includes(p.id)))
        )

    const caughtPokemons = combineLatest([caughtPokemonIds, pokemonObservable])
        .pipe(
            map(([ids, pokemons]) => pokemons.filter(p => ids.includes(p.id)))
        )
    
    return {
        pokemonIdsBeingCaught,
        pokemonIdsBeingReleased,
        freePokemons,
        caughtPokemons
    }
}

export const pokemonFetcher = (id: number) => fromFetch<IPokemon>(`https://pokeapi.co/api/v2/pokemon/${id}`, {selector: res => res.json()})