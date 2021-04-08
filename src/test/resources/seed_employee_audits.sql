--populate positions
INSERT INTO `positions` (`id`, `name`, `manager_position_id`, `created_at`)
VALUES
	(10001, 'test_boss', NULL, '2021-01-25 17:43:14');

INSERT INTO `positions` (`id`, `name`, `manager_position_id`, `created_at`)
VALUES
	(10002, 'test_worker', 10001, '2021-01-25 17:43:14');

--populate employees
INSERT INTO `employees` (`id`, `position_id`, `manager_id`, `name`, `salary_amount`, `salary_currency`, `created_at`)
VALUES
	(10001, 10001, NULL, 'Boss1', 3100, 'USD', '2021-01-13 10:45:54'),
    (10002, 10001, NULL, 'Boss2', 3200, 'USD', '2021-01-13 10:45:54');

INSERT INTO `employees` (`id`, `position_id`, `manager_id`, `name`, `salary_amount`, `salary_currency`, `created_at`)
VALUES
	(10003, 10002, 10001, 'Worker1', 1301, 'USD', '2021-01-13 10:10:03'),
    (10004, 10002, 10002, 'Worker2', 1302, 'USD', '2021-01-13 10:10:03'),
	(10005, 10002, 10001, 'Worker3', 1303, 'USD', '2021-01-13 10:10:03');


--populate employees_audit
INSERT INTO `employees_audit` (`id`, `employee_id`, `position_id`, `manager_id`, `name`, `salary_amount`, `salary_currency`, `created_at`)
VALUES
--    audit for initial record creation
	(10001, 10003, 10002, 10001, 'Worker1', 1301, 'USD', '2021-01-13 10:12:32'),
--	audit for update
	(10002, 10003, 10002, 10002, 'Worker1', 1301, 'USD', '2021-04-01 23:45:32'),

    (10003, 10004, 10002, 10001, 'Worker2', 1301, 'USD', '2021-01-13 10:12:32'),
    (10004, 10004, 10002, 10002, 'Worker2', 1301, 'USD', '2021-01-14 16:30:00'),

    (10005, 10005, 10002, 10001, 'Worker3', 1301, 'USD', '2021-01-13 10:12:40'),
    (10006, 10005, 10002, 10002, 'Worker3', 1301, 'USD', '2021-01-14 13:02:54'),
    (10007, 10005, 10002, 10001, 'Worker3', 1301, 'USD', '2021-01-15 09:15:19');

