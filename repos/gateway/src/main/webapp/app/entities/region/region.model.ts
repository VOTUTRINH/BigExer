import { ICountry } from 'app/entities/country/country.model';

export interface IRegion {
  id?: number;
  regionName?: string | null;
  countries?: ICountry[] | null;
}

export class Region implements IRegion {
  constructor(public id?: number, public regionName?: string | null, public countries?: ICountry[] | null) {}
}

export function getRegionIdentifier(region: IRegion): number | undefined {
  return region.id;
}
