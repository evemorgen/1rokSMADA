(define (problem zombie-escape-video)
	(:domain zombie-escape)
	(:objects
		man woman janitor professor - human
		left right - location)
	(:init
		(= (time-counter) 0)
		(= (time man) 1)
		(= (time woman) 2)
		(= (time janitor) 5)
		(= (time professor) 10)
		(on-side man left)
		(on-side woman left)
		(on-side janitor left)
		(on-side professor left)
		(latern-side left)
    )
	(:goal (and 
			(on-side man right)
			(on-side woman right)
			(on-side janitor right)
			(on-side professor right)
            (<= (time-counter) 17)
		)
    )
	(:metric minimize (time-counter))
)