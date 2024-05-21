package me.butter.impl.scenario;

import me.butter.api.scenario.Scenario;
import me.butter.api.scenario.ScenarioHandler;
import me.butter.impl.scenario.list.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ScenarioHandlerImpl implements ScenarioHandler {

    List<Scenario> scenarios;

    public ScenarioHandlerImpl() {
        scenarios = new ArrayList<>();

        scenarios.add(new CutCleanScenario());
        scenarios.add(new FireLessScenario());
        scenarios.add(new HasteyBoysScenario());
        scenarios.add(new TimberScenario());
        scenarios.add(new NoDropScenario());
    }

    @Override
    public List<Scenario> getAllScenarios() {
        return scenarios;
    }

    @Override
    public List<Scenario> getEnabledScenarios() {
        return scenarios.stream().filter(Scenario::isEnabled).collect(Collectors.toList());
    }

    @Override
    public void addScenario(Scenario scenario) {
        if(!scenarios.contains(scenario)) {
            scenarios.add(scenario);
        }
    }

    @Override
    public void removeScenario(Class<? extends Scenario> scenarioClass) {
        scenarios.removeIf(scenario -> scenario.getClass() == scenarioClass);
    }

    @Override
    public Scenario getScenario(Class<? extends Scenario> scenarioClass) {
        return scenarios.stream().filter(scenario -> scenario.getClass() == scenarioClass).findFirst().orElse(null);
    }
}
