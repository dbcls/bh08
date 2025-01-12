-- ## 4: Assigned, Active Tickets by Owner ## --

-- 
-- List assigned tickets, group by ticket owner, sorted by priority.



SELECT p.value AS __color__,
   owner AS __group__,
   id AS ticket, summary, component, milestone, t.type AS type, time AS created,
   changetime AS _changetime, description AS _description,
   reporter AS _reporter
  FROM ticket t
  LEFT JOIN enum p ON p.name = t.priority AND p.type = 'priority'
  WHERE status = 'assigned'
  ORDER BY owner, p.value, t.type, time
