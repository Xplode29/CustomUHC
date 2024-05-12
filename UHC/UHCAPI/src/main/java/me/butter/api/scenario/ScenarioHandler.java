package me.butter.api.scenario;

import java.util.List;

public interface ScenarioHandler {

    List<Scenario> getAllScenarios(); List<Scenario> getEnabledScenarios();

    void addScenario(Scenario scenario); void removeScenario(Class<? extends Scenario> scenarioClass);

    Scenario getScenario(Class<? extends Scenario> scenarioClass);
}
