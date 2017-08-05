program
	var N as int ;
	var SQRT as int ;
	
begin
	N := readint ;
	SQRT := 0 ;

	% go until SQRT exceeds the square root of N
	while SQRT * SQRT <= N do
		SQRT := SQRT + 1 ;
	end ;

	SQRT := SQRT - 1 ; % subtract one SQRT is <= sqrt(N)

	writeint SQRT ;

end