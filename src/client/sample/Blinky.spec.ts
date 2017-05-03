import {Ghost} from './Ghost';
import {Blinky} from './Blinky';

describe('Blinky', () => {

	it('should return the expected value from getValue()', () => {
		const ghost: Ghost = new Blinky();
		expect(ghost.getValue()).toEqual(47);
	});
});
