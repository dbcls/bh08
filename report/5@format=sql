-- ## 5: Assigned, Active Tickets by Owner (Full Description) ## --

-- 
-- List tickets assigned, group by ticket owner.
-- This report demonstrates the use of full-row display.


SELECT p.value AS __color__,
   owner AS __group__,
   id AS ticket, summary, component, milestone, t.type AS type, time AS created,
   description AS _description_,
   changetime AS _changetime, reporter AS _reporter
  FROM ticket t
  LEFT JOIN enum p ON p.name = t.priority AND p.type = 'priority'
  WHERE status = 'assigned'
  ORDER BY owner, p.value, t.type, time
