export interface IProba {
  id?: number;
  broj?: number | null;
  ime?: string | null;
}

export class Proba implements IProba {
  constructor(public id?: number, public broj?: number | null, public ime?: string | null) {}
}

export function getProbaIdentifier(proba: IProba): number | undefined {
  return proba.id;
}
