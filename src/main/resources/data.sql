INSERT INTO jackpot_template
(id, name, initial_contribution, contribution_type, initial_contribution_percentage, pool_limit, reward_evaluation_type,
 initial_reward_change_percentage)
VALUES ('11111111-1111-1111-1111-111111111111', '50k Megawin!', 1000.0, 'FIXED', 10.0, 50000.0, 'FIXED', 3.0);

INSERT INTO jackpot_template
(id, name, initial_contribution, contribution_type, initial_contribution_percentage, pool_limit, reward_evaluation_type,
 initial_reward_change_percentage)
VALUES ('22222222-2222-2222-2222-222222222222', '1000 regular Jackpot', 50.0, 'VARIABLE', 20.0, 1000.0, 'VARIABLE',
        1.0);


INSERT INTO jackpot
(id, jackpot_template_id, name, contribution_type, initial_contribution_percentage, total_contribution, pool_limit,
 reward_evaluation_type, initial_reward_chance_percentage, status)
VALUES ('11111111-1111-1111-1111-22222222', '11111111-1111-1111-1111-111111111111',
        '50k Megawin!', 'FIXED', 10.0, 1000.0, 50000.0, 'FIXED', 3.0, 'IN_PROGESS');

INSERT INTO jackpot
(id, jackpot_template_id, name, contribution_type, initial_contribution_percentage, total_contribution, pool_limit,
 reward_evaluation_type, initial_reward_chance_percentage, status)
VALUES ('22222222-2222-2222-2222-111111111', '22222222-2222-2222-2222-222222222222',
        '1000 regular Jackpot', 'VARIABLE', 20.0, 50.0, 1000.0, 'VARIABLE', 1.0, 'IN_PROGESS');

