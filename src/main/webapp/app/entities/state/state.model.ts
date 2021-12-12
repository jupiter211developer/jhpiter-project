import { ICounty } from 'app/entities/county/county.model';

export interface IState {
  id?: number;
  stateName?: string | null;
  stateAbbr?: string | null;
  subRegion?: string | null;
  stFips?: string | null;
  counties?: ICounty[] | null;
}

export class State implements IState {
  constructor(
    public id?: number,
    public stateName?: string | null,
    public stateAbbr?: string | null,
    public subRegion?: string | null,
    public stFips?: string | null,
    public counties?: ICounty[] | null
  ) {}
}

export function getStateIdentifier(state: IState): number | undefined {
  return state.id;
}
